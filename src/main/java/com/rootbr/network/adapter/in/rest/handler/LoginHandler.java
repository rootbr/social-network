package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.TokenService;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class LoginHandler implements RestHandler {
  private final TokenService tokenService;

  public LoginHandler(final TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal p, final SocialNetworkApplication application, final String[] pathVariables)
      throws IOException {
    String userId = null, password = null;
    try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME && "id".equals(parser.getText())) {
          parser.nextToken();
          userId = parser.getValueAsString();
        } else if (jsonToken == JsonToken.FIELD_NAME && "password".equals(parser.getText())) {
          parser.nextToken();
          password = parser.getValueAsString();
        }
      }
    }
    if (userId == null || password == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final Principal principal = application.login(userId, password);

    if (principal == null) {
      exchange.sendResponseHeaders(404, -1);
      return;
    }

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("token", principal.generateToken(tokenService));
      generator.writeEndObject();
    }
  }
}
