package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.in.rest.JsonHttpHandler;
import com.rootbr.network.adapter.in.rest.HttpMethod;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.util.List;

public class PostLogin extends JsonHttpHandler {


  public PostLogin(final HttpServer server, final JsonFactory jsonFactory,
      final SocialNetworkApplication application, final List<Filter> filters) {
    super(server, "/login", HttpMethod.POST, null, jsonFactory, application, filters);
  }

  @Override
  protected void doHandle(final HttpExchange exchange) throws IOException {
    String id = null, password = null;
    try (final JsonParser parser = jsonFactory.createParser(exchange.getRequestBody())) {
      JsonToken jsonToken;
      while ((jsonToken = parser.nextToken()) != null) {
        if (jsonToken == JsonToken.FIELD_NAME && "id".equals(parser.getText())) {
          parser.nextToken();
          id = parser.getValueAsString();
        } else if (jsonToken == JsonToken.FIELD_NAME && "password".equals(parser.getText())) {
          parser.nextToken();
          password = parser.getValueAsString();
        }
      }
    }
    if (id == null || password == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    final Principal principal = application.login(id, password);

    if (principal == null) {
      exchange.sendResponseHeaders(401, -1);
      return;
    }

    exchange.sendResponseHeaders(200, 0);
    try (final JsonGenerator generator = jsonFactory.createGenerator(exchange.getResponseBody())) {
      generator.writeStartObject();
      generator.writeStringField("token", principal.generateJwt());
      generator.writeEndObject();
    }
  }
}
