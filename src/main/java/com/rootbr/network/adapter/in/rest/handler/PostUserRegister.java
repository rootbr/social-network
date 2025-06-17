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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PostUserRegister extends JsonHttpHandler {

  public PostUserRegister(final HttpServer server, final JsonFactory jsonFactory, final SocialNetworkApplication application,
      final List<Filter> filters) {
    super(server, "/user/register", HttpMethod.POST, null, jsonFactory, application, filters);
  }

  @Override
  protected void doHandle(final HttpExchange exchange) throws IOException {
    String firstName = null, lastName = null, biography = null, city = null, password = null;
    LocalDate birthdate = null;
    try (final JsonParser parser = jsonFactory.createParser(exchange.getRequestBody())) {
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
              lastName = parser.getValueAsString();
              break;
            case "birthdate":
              birthdate = LocalDate.parse(parser.getValueAsString());
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
            default:
              throw new IllegalArgumentException("Unknown field name: " + fieldName);
          }
        }
      }
    }

    final String userId = UUID.randomUUID().toString();
    application.createPrincipal(userId, password)
        .registerUser(userId, firstName, lastName, city, birthdate, biography);

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = jsonFactory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("userId", userId);
      generator.writeEndObject();
    }
  }
}
