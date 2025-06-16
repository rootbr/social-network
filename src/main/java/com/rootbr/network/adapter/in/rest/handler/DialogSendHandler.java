package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class DialogSendHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application)
      throws IOException {
    final String path = exchange.getRequestURI().getPath();
    final String toUserId = extractPathVariable(path, "/dialog/", "/send");
    
    if (toUserId == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

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

    principal.execute(application.sendMessageCommand(toUserId, text));
    exchange.sendResponseHeaders(200, -1);
  }

  private String extractPathVariable(final String path, final String prefix, final String suffix) {
    if (path.startsWith(prefix) && path.endsWith(suffix)) {
      final int start = prefix.length();
      final int end = path.length() - suffix.length();
      if (start < end) {
        return path.substring(start, end);
      }
    }
    return null;
  }
}