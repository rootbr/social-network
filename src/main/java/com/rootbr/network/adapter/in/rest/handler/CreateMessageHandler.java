// src/main/java/com/rootbr/legalai/adapter/in/rest/handler/CreateMessageHandler.java

package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.time.Instant;

public class CreateMessageHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application) throws IOException {
    final String chatId = exchange.getRequestURI().getPath().substring(11, 47);

//export interface Message {
//  id: number;
//  content: string;
//  timestamp: string;
//  role: string;
//  isError: boolean;
//  error?: string;
//}
//
//export interface Question {
//  title: string;
//  description: string;
//}

//  question: Question | null;

    String questionKey = null, questionTitle = null, questionDescription = null;
    int id = -1;
    String content = null, role = null;
    Instant timestamp = null;
    boolean isError = false;
    try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME) {
          final String fieldName = parser.getText();
          parser.nextToken();
          switch (fieldName) {
            case "message":
              while ((jsonToken = parser.nextToken()) != JsonToken.END_OBJECT) {
                if (jsonToken == JsonToken.FIELD_NAME) {
                  final String fieldName2 = parser.getText();
                  parser.nextToken();
                  switch (fieldName2) {
                    case "id":
                      id = parser.getValueAsInt();
                      break;
                    case "content":
                      content = parser.getValueAsString();
                      break;
                    case "timestamp":
                      timestamp = Instant.parse(parser.getValueAsString());
                      break;
                    case "role":
                      role = parser.getValueAsString();
                      break;
                    case "isError":
                      isError = parser.getValueAsBoolean();
                      break;
                  }
                }
              }
              break;
            case "question":
              while ((jsonToken = parser.nextToken()) != JsonToken.END_OBJECT) {
                if (jsonToken == JsonToken.FIELD_NAME) {
                  final String fieldName2 = parser.getText();
                  parser.nextToken();
                  switch (fieldName2) {
                    case "key":
                      questionKey = parser.getValueAsString();
                      break;
                    case "title":
                      questionTitle = parser.getValueAsString();
                      break;
                    case "description":
                      questionDescription = parser.getValueAsString();
                      break;
                  }
                }
              }
              break;
          }
        }
      }
    }
    principal.execute(application.newMessageCommand(chatId, id, questionKey, content,
        (aiMessageId, aiResponse, nextQuestion) -> {
          exchange.sendResponseHeaders(200, 0);
          try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
            generator.writeStartObject();
            generator.writeObjectFieldStart("message");
            generator.writeNumberField("id", aiMessageId);
            generator.writeStringField("content", aiResponse);
            generator.writeStringField("timestamp", Instant.now().toString());
            generator.writeStringField("role", "assistant");
            generator.writeBooleanField("isError", false);
            generator.writeEndObject();
            if (nextQuestion != null) {
              generator.writeObjectFieldStart("question");
              generator.writeStringField("key", nextQuestion.key());
              generator.writeStringField("title", nextQuestion.title());
              generator.writeStringField("description", nextQuestion.description());
              generator.writeEndObject();
            } else {
              generator.writeNullField("question");
            }
            generator.writeEndObject();
          }
        }
    ));
  }
}