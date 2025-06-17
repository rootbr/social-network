package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.UsersVisitor;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class GetUserHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application, final String[] pathVariables)
      throws IOException {
    if (pathVariables.length == 0) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    
    final String userId = pathVariables[0];

    final String[] userData = new String[6]; // id, firstName, lastName, birthdate, biography, city
    final UsersVisitor visitor = new UsersVisitor() {
      @Override
      public void visitUser(final String id, final String firstName, final String lastName,
                          final String birthdate, final String biography, final String city) {
        userData[0] = id;
        userData[1] = firstName;
        userData[2] = lastName;
        userData[3] = birthdate;
        userData[4] = biography;
        userData[5] = city;
      }
    };

    principal.execute(application.getUserByIdCommand(userId, visitor));

    if (userData[0] == null) {
      exchange.sendResponseHeaders(404, -1);
      return;
    }

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("id", userData[0]);
      generator.writeStringField("first_name", userData[1]);
      generator.writeStringField("second_name", userData[2]);
      generator.writeStringField("birthdate", userData[3]);
      generator.writeStringField("biography", userData[4]);
      generator.writeStringField("city", userData[5]);
      generator.writeEndObject();
    }
  }

}
