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

public class GetMessagesHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application,
      final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters) throws IOException {
    final String path = exchange.getRequestURI().getPath();
    final String chatId = path.substring(11, 47);
    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      principal.execute(application.readMessagesCommand(
          chatId,
          (messageId, role, content, timestamp, isFirst, isLast) -> {
            if (isFirst)
              generator.writeArrayFieldStart("messages");
            generator.writeStartObject();
            generator.writeNumberField("id", messageId);
            generator.writeStringField("role", role);
            generator.writeStringField("content", content);
            generator.writeStringField("timestamp", timestamp);
            generator.writeEndObject();
            if (isLast)
              generator.writeEndArray();
          },
          nextQuestion -> {
            if (nextQuestion != null) {
              generator.writeObjectFieldStart("question");
              generator.writeStringField("key", nextQuestion.key());
              generator.writeStringField("title", nextQuestion.title());
              generator.writeStringField("description", nextQuestion.description());
              generator.writeEndObject();
            } else {
              generator.writeNullField("question");
            }
          }
          ));
      generator.writeEndObject();
    }
  }
}
