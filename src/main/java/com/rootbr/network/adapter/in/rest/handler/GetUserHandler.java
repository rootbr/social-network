package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.UsersVisitor;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GetUserHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application, final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    if (pathVariables.length == 0) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    final String userId = pathVariables[0];
    final boolean[] added = {false};
    principal.execute(application.getUserByIdCommand(userId, new UsersVisitor() {
      @Override
      public void visitUser(final String id, final String firstName, final String lastName,
          final String birthdate, final String biography, final String city) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
          generator.writeStartObject();
          generator.writeStringField("id", id);
          generator.writeStringField("first_name", firstName);
          generator.writeStringField("second_name", lastName);
          generator.writeStringField("birthdate", birthdate);
          generator.writeStringField("biography", biography);
          generator.writeStringField("city", city);
          generator.writeEndObject();
        }
        added[0] = true;
      }
    }));
    if (!added[0]) {
      exchange.sendResponseHeaders(404, -1);
    }
  }
}
