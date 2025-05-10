package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.HttpHandler;
import com.rootbr.network.adapter.in.rest.HttpMethod;
import com.rootbr.network.adapter.in.rest.RestService;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

public class PostUserRegister extends HttpHandler {

  private final JsonFactory factory;
  private final SocialNetworkApplication socialNetworkApplication;

  public PostUserRegister(final HttpServer server, final JsonFactory factory,
      final SocialNetworkApplication socialNetworkApplication) {
    super(server, "/user/register", HttpMethod.POST, null);
    this.factory = factory;
    this.socialNetworkApplication = socialNetworkApplication;
  }

  @Override
  protected void doHandle(final HttpExchange exchange) throws IOException {
    String firstName = null, lastName = null, biography = null, city = null, password = null;
    LocalDate birthdate = null;
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
//    java.security.Principal principal = socialNetworkApplication.createPrincipal(userId,
//        password);
//    principal.registerUser(userId, firstName, lastName, city, birthdate, biography);


    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("userId", userId);
      generator.writeEndObject();
    }
  }
}
