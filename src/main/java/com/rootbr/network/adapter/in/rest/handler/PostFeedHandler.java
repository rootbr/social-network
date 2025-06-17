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

public class PostFeedHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application,
      final String[] pathVariables,
      final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    final Map<String, List<String>> queryParams = queryParameters.apply(exchange);
    final int offset = queryParams.containsKey("offset") ?
        Integer.parseInt(queryParams.get("offset").getFirst()) : 0;
    final int limit = queryParams.containsKey("limit") ?
        Integer.parseInt(queryParams.get("limit").getFirst()) : 10;
    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartArray();
      principal.execute(application.getUserFeedCommand(offset, limit, (id, text, authorUserId) -> {
        generator.writeStartObject();
        generator.writeStringField("id", id);
        generator.writeStringField("text", text);
        generator.writeStringField("author_user_id", authorUserId);
        generator.writeEndObject();
      }));
      generator.writeEndArray();
    }
  }
}