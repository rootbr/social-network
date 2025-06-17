package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
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
    final String postId = pathVariables[0];
    if (postId == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    final boolean[] success = new boolean[1];
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      principal.execute(application.getPostByIdCommand(postId, (id, text, authorUserId) -> {
        exchange.sendResponseHeaders(200, 0);
        generator.writeStartObject();
        generator.writeStringField("id", id);
        generator.writeStringField("text", text);
        generator.writeStringField("author_user_id", authorUserId);
        generator.writeEndObject();
        success[0] = true;
      }));
    }
    if (!success[0]) {
      exchange.sendResponseHeaders(404, -1);
    }
  }
}