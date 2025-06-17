package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UserSearchHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application, final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    final Map<String, List<String>> queryParams = queryParameters.apply(exchange);
    final String firstName = queryParams.get("first_name").getLast();
    final String lastName = queryParams.get("last_name").getLast();
    if (firstName == null || lastName == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartArray();
      principal.execute(application.searchUsersCommand(firstName, lastName, (id, firstNameValue, lastNameValue, birthdate, biography, city) -> {
            generator.writeStartObject();
            generator.writeStringField("id", id);
            generator.writeStringField("first_name", firstNameValue);
            generator.writeStringField("second_name", lastNameValue);
            generator.writeStringField("birthdate", birthdate);
            generator.writeStringField("biography", biography);
            generator.writeStringField("city", city);
            generator.writeEndObject();
          }));
      generator.writeEndArray();
    }
  }
}