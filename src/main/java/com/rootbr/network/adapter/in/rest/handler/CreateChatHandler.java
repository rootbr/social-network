// src/main/java/com/rootbr/legalai/adapter/in/rest/handler/CreateChatHandler.java

package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.legalai.application.LegalAiApplication;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class CreateChatHandler implements RestHandler {
  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory, final Principal principal, final SocialNetworkApplication application) throws IOException {
    final String chatId = exchange.getRequestURI().getPath().substring(11);
    principal.execute(application.createChatCommand(chatId, (id, title, lastMessage, date) -> {
      exchange.sendResponseHeaders(200, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();
        generator.writeStringField("id", chatId);
        generator.writeStringField("title", title);
        generator.writeStringField("lastMessage", lastMessage);
        generator.writeStringField("date", date);
        generator.writeEndObject();
      }
    }));
  }
}