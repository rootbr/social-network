package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PostCreateHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application,
      final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    String text = null;
    try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME && "text".equals(parser.getText())) {
          parser.nextToken();
          text = parser.getValueAsString();
        }
      }
    }

    if (text == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final String postId = UUID.randomUUID().toString();
    principal.execute(application.createPostCommand(postId, text));

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeString(postId);
    }
  }
}