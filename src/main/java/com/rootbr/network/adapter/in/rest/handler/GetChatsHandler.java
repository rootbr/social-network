package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class GetChatsHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory, final Principal principal, final SocialNetworkApplication application, final String[] pathVariables) throws IOException {
    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartArray();
      principal.execute(application.readChatsCommand((id, title, lastMessage, date) -> {
        try {
          generator.writeStartObject();
          generator.writeStringField("id", id);
          generator.writeStringField("title", title);
          generator.writeStringField("lastMessage", lastMessage);
          generator.writeStringField("date", date);
          generator.writeEndObject();
        } catch (final IOException e) {
          throw new RuntimeException(e);
        }
      }));
      generator.writeEndArray();
    }
  }
}
