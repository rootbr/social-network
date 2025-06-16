package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class PostUpdateHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application)
      throws IOException {
    String id = null, text = null;
    try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME) {
          final String fieldName = parser.getText();
          parser.nextToken();
          switch (fieldName) {
            case "id":
              id = parser.getValueAsString();
              break;
            case "text":
              text = parser.getValueAsString();
              break;
          }
        }
      }
    }

    if (id == null || text == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    principal.execute(application.updatePostCommand(id, text));
    exchange.sendResponseHeaders(200, -1);
  }
}