package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.PostVisitor;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PostGetHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application,
      final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    final String path = exchange.getRequestURI().getPath();
    final String postId = extractPathVariable(path, "/post/get/");

    if (postId == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final String[] postData = new String[3]; // id, text, author_user_id
    final PostVisitor visitor = new PostVisitor() {
      @Override
      public void visitPost(final String id, final String text, final String authorUserId) {
        postData[0] = id;
        postData[1] = text;
        postData[2] = authorUserId;
      }
    };

    principal.execute(application.getPostByIdCommand(postId, visitor));

    if (postData[0] == null) {
      exchange.sendResponseHeaders(404, -1);
      return;
    }

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("id", postData[0]);
      generator.writeStringField("text", postData[1]);
      generator.writeStringField("author_user_id", postData[2]);
      generator.writeEndObject();
    }
  }

  private String extractPathVariable(final String path, final String prefix) {
    if (path.startsWith(prefix)) {
      return path.substring(prefix.length());
    }
    return null;
  }
}