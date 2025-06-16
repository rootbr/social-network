package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.TokenService;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.legalai.application.LegalAiApplication;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class TokenValidateHandler implements RestHandler {
  private final TokenService tokenService;

  public TokenValidateHandler(final TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal p, final SocialNetworkApplication application)
      throws IOException {
    String token = null;
    try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME && "token".equals(parser.getText())) {
          parser.nextToken();
          token = parser.getValueAsString();
        }
      }
    }
    if (token == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final boolean valid = tokenService.validateToken(token);

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeBooleanField("valid", valid);
      generator.writeEndObject();
    }
  }
}
