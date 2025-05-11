package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.JsonHttpHandler;
import com.rootbr.network.adapter.in.rest.HttpMethod;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

public class GetUserById extends JsonHttpHandler {

  public GetUserById(final HttpServer server, final JsonFactory jsonFactory,
      final SocialNetworkApplication application, final Authenticator authenticator) {
    super(server, "/user/", HttpMethod.GET, authenticator, jsonFactory, application);
  }

  @Override
  public void doHandle(final HttpExchange exchange) throws IOException {
    final Principal principal = ((Principal) exchange.getPrincipal());
    final String userId = exchange.getRequestURI().getPath().substring(10);
    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = jsonFactory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      principal.readUser(userId, (id, firstName, lastName, city, birthdate, biography) -> {
        generator.writeStringField("userId", id);
        generator.writeStringField("firstName", firstName);
        generator.writeStringField("secondName", lastName);
        generator.writeStringField("city", city);
        generator.writeStringField("birthdate", birthdate.toString());
        generator.writeStringField("biography", biography);
      });
      generator.writeEndObject();
    }
  }
}
