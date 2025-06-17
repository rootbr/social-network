package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.PostVisitor;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PostFeedHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application,
      final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    final URI uri = exchange.getRequestURI();
    final Map<String, String> queryParams = parseQueryParameters(uri.getQuery());

    final int offset = queryParams.containsKey("offset") ?
        Integer.parseInt(queryParams.get("offset")) : 0;
    final int limit = queryParams.containsKey("limit") ?
        Integer.parseInt(queryParams.get("limit")) : 10;

    final List<Map<String, Object>> posts = new ArrayList<>();
    final PostVisitor visitor = new PostVisitor() {
      @Override
      public void visitPost(final String id, final String text, final String authorUserId) {
        final Map<String, Object> post = new HashMap<>();
        post.put("id", id);
        post.put("text", text);
        post.put("author_user_id", authorUserId);
        posts.add(post);
      }
    };

    principal.execute(application.getUserFeedCommand(offset, limit, visitor));

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartArray();
      for (final Map<String, Object> post : posts) {
        generator.writeStartObject();
        generator.writeStringField("id", (String) post.get("id"));
        generator.writeStringField("text", (String) post.get("text"));
        generator.writeStringField("author_user_id", (String) post.get("author_user_id"));
        generator.writeEndObject();
      }
      generator.writeEndArray();
    }
  }

  private Map<String, String> parseQueryParameters(final String query) {
    final Map<String, String> params = new HashMap<>();
    if (query != null) {
      final String[] pairs = query.split("&");
      for (final String pair : pairs) {
        final String[] keyValue = pair.split("=");
        if (keyValue.length == 2) {
          params.put(keyValue[0], keyValue[1]);
        }
      }
    }
    return params;
  }
}