package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.HttpMethod;
import com.rootbr.network.adapter.in.rest.JsonHttpHandler;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class PostApiUserRegister extends JsonHttpHandler {

  public PostApiUserRegister(final HttpServer server, final JsonFactory jsonFactory, final SocialNetworkApplication application,
      final List<Filter> filters) {
    super(server, "/api/auth/register", HttpMethod.POST, null, jsonFactory, application, filters);
  }

  @Override
  protected void doHandle(final HttpExchange exchange) throws IOException {
    String email = null, password = null;
    try (final JsonParser parser = jsonFactory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME) {
          final String fieldName = parser.getText();
          parser.nextToken();
          switch (fieldName) {
            case "email":
              email = parser.getValueAsString();
              break;
            case "password":
              password = parser.getValueAsString();
              break;
          }
        }
      }
    }

    final String userId = UUID.randomUUID().toString();
    application.createPrincipal(userId, password)
        .registerUser(email, email, email, email, null, email);

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = jsonFactory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("userId", userId);
      generator.writeEndObject();
    }
  }
}
