package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.legalai.application.LegalAiApplication;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.UUID;

public class UserRegisterHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application)
      throws IOException {
    String firstName = null, secondName = null, birthdate = null, biography = null, city = null, password = null;
    try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME) {
          final String fieldName = parser.getText();
          parser.nextToken();
          switch (fieldName) {
            case "first_name":
              firstName = parser.getValueAsString();
              break;
            case "second_name":
              secondName = parser.getValueAsString();
              break;
            case "birthdate":
              birthdate = parser.getValueAsString();
              break;
            case "biography":
              biography = parser.getValueAsString();
              break;
            case "city":
              city = parser.getValueAsString();
              break;
            case "password":
              password = parser.getValueAsString();
              break;
          }
        }
      }
    }

    if (firstName == null || secondName == null || password == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final String userId = UUID.randomUUID().toString();
    principal.execute(application.registerUserCommand(userId, firstName, secondName, birthdate, biography, city, password));

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("user_id", userId);
      generator.writeEndObject();
    }
  }
}
