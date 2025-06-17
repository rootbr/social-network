package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;

public class DeleteChatHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application) throws IOException {

    // Извлекаем chatId из URL пути
    final String path = exchange.getRequestURI().getPath();
    final String chatId = path.substring(11); // Убираем "/api/chats/" (11 символов)

    // Потребляем тело запроса (если есть)
    consumeRequestBody(exchange);

    // Выполняем команду удаления чата
    principal.execute(application.deleteChatCommand(chatId));

    // Возвращаем статус 204 No Content (успешное удаление без тела ответа)
    exchange.sendResponseHeaders(204, -1);
  }

  // Метод для потребления тела запроса (обязательно для DELETE запросов)
  private void consumeRequestBody(final HttpExchange exchange) throws IOException {
    final byte[] buffer = new byte[4096];
    try (final InputStream inputStream = exchange.getRequestBody()) {
      while (inputStream.read(buffer) != -1) {
        // Просто читаем и игнорируем данные
      }
    }
  }
}