package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.HttpHandler;
import com.rootbr.network.adapter.in.rest.HttpMethod;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.util.UUID;

public class PostLogin extends HttpHandler {

  private final JsonFactory factory;
  private final SocialNetworkApplication socialNetworkApplication;

  public PostLogin(final HttpServer server, final JsonFactory factory,
      final SocialNetworkApplication socialNetworkApplication) {
    super(server, "/login", HttpMethod.POST, null);
    this.factory = factory;
    this.socialNetworkApplication = socialNetworkApplication;
  }

  @Override
  protected void doHandle(final HttpExchange exchange) throws IOException {
    String id = null, password = null;
    try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME && "id".equals(parser.getText())) {
          parser.nextToken();
          id = parser.getValueAsString();
        } else if (jsonToken == JsonToken.FIELD_NAME && "password".equals(parser.getText())) {
          parser.nextToken();
          password = parser.getValueAsString();
        }
      }
    }
    if (id == null || password == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final String userId = UUID.randomUUID().toString();
    java.security.Principal principal = socialNetworkApplication.login(userId, password);

    if (principal == null) {
      exchange.sendResponseHeaders(401, -1);
      return;
    }

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
//      generator.writeStringField("token", principal.generateToken());
      generator.writeEndObject();
    }
  }
}
