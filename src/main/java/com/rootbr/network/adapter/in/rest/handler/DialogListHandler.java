package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.DialogMessageVisitor;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogListHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application)
      throws IOException {
    final String path = exchange.getRequestURI().getPath();
    final String otherUserId = extractPathVariable(path, "/dialog/", "/list");
    
    if (otherUserId == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final List<Map<String, Object>> messages = new ArrayList<>();
    final DialogMessageVisitor visitor = new DialogMessageVisitor() {
      @Override
      public void visitMessage(final String from, final String to, final String text) {
        final Map<String, Object> message = new HashMap<>();
        message.put("from", from);
        message.put("to", to);
        message.put("text", text);
        messages.add(message);
      }
    };

    principal.execute(application.getDialogMessagesCommand(otherUserId, visitor));

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartArray();
      for (final Map<String, Object> message : messages) {
        generator.writeStartObject();
        generator.writeStringField("from", (String) message.get("from"));
        generator.writeStringField("to", (String) message.get("to"));
        generator.writeStringField("text", (String) message.get("text"));
        generator.writeEndObject();
      }
      generator.writeEndArray();
    }
  }

  private String extractPathVariable(final String path, final String prefix, final String suffix) {
    if (path.startsWith(prefix) && path.endsWith(suffix)) {
      final int start = prefix.length();
      final int end = path.length() - suffix.length();
      if (start < end) {
        return path.substring(start, end);
      }
    }
    return null;
  }
}