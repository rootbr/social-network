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

public class DialogListHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application,
      final String[] pathVariables,
      final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    final String otherUserId = pathVariables[0];
    if (otherUserId == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }
    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartArray();
      principal.execute(application.getDialogMessagesCommand(otherUserId, (from, to, text) -> {
        generator.writeStartObject();
        generator.writeStringField("from", from);
        generator.writeStringField("to", to);
        generator.writeStringField("text", text);
        generator.writeEndObject();
      }));
      generator.writeEndArray();
    }
  }
}