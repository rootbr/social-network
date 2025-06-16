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
import java.util.UUID;

public class PostCreateHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application)
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