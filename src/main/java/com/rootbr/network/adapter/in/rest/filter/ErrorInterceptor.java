package com.rootbr.network.adapter.in.rest.filter;

import static org.slf4j.LoggerFactory.getLogger;

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
import org.slf4j.Logger;

public class ErrorInterceptor implements RestHandler {

  private static final Logger log = getLogger(ErrorInterceptor.class);
  private final RestHandler delegate;

  public ErrorInterceptor(final RestHandler delegate) {
    this.delegate = delegate;
  }

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application, final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters) throws IOException {
    try {
      delegate.handle(exchange, factory, principal, application, pathVariables, queryParameters);
    } catch (final Exception e) {
      log.error("Error handling request", e);
      exchange.sendResponseHeaders(500, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();
        generator.writeStringField("error", e.getMessage());
        generator.writeEndObject();
      }
    }
  }
}
