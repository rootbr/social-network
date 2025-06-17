package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;

public class ContractVariablesHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application) throws IOException {
    final String chatId = exchange.getRequestURI().getPath().substring(11, 47);

    // Потребляем тело запроса (если есть)
    consumeRequestBody(exchange);

    try {
      exchange.sendResponseHeaders(200, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();

        principal.execute(application.contractVariablesCommand(chatId, (String key, String value) -> {
          try {
            generator.writeStringField(key, value);
          } catch (final IOException e) {
            throw new RuntimeException(e);
          }
        }));

        generator.writeEndObject();
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  // Метод для потребления тела запроса
  private void consumeRequestBody(final HttpExchange exchange) throws IOException {
    final byte[] buffer = new byte[4096];
    try (final InputStream inputStream = exchange.getRequestBody()) {
      while (inputStream.read(buffer) != -1) {
        // Просто читаем и игнорируем данные
      }
    }
  }
}