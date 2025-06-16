package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.UsersVisitor;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserSearchHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application)
      throws IOException {
    final URI uri = exchange.getRequestURI();
    final Map<String, String> queryParams = parseQueryParameters(uri.getQuery());
    
    final String firstName = queryParams.get("first_name");
    final String lastName = queryParams.get("last_name");
    
    if (firstName == null || lastName == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final List<Map<String, Object>> users = new ArrayList<>();
    final UsersVisitor visitor = new UsersVisitor() {
      @Override
      public void visitUser(final String id, final String firstNameValue, final String lastNameValue, 
                          final String birthdate, final String biography, final String city) {
        final Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("first_name", firstNameValue);
        user.put("second_name", lastNameValue);
        user.put("birthdate", birthdate);
        user.put("biography", biography);
        user.put("city", city);
        users.add(user);
      }
    };

    principal.execute(application.searchUsersCommand(firstName, lastName, visitor));

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartArray();
      for (final Map<String, Object> user : users) {
        generator.writeStartObject();
        generator.writeStringField("id", (String) user.get("id"));
        generator.writeStringField("first_name", (String) user.get("first_name"));
        generator.writeStringField("second_name", (String) user.get("second_name"));
        generator.writeStringField("birthdate", (String) user.get("birthdate"));
        generator.writeStringField("biography", (String) user.get("biography"));
        generator.writeStringField("city", (String) user.get("city"));
        generator.writeEndObject();
      }
      generator.writeEndArray();
    }
  }

  private Map<String, String> parseQueryParameters(final String query) {
    final Map<String, String> params = new HashMap<>();
    if (query != null) {
      final String[] pairs = query.split("&");
      for (final String pair : pairs) {
        final String[] keyValue = pair.split("=");
        if (keyValue.length == 2) {
          params.put(keyValue[0], keyValue[1]);
        }
      }
    }
    return params;
  }
}