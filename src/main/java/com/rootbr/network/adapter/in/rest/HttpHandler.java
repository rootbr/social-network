package com.rootbr.network.adapter.in.rest;

import static com.rootbr.network.adapter.in.rest.JsonFilter.CONTENT_TYPE_APPLICATION_JSON;
import static com.rootbr.network.adapter.in.rest.JsonFilter.HEADER_CONTENT_TYPE;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;

public abstract class HttpHandler implements com.sun.net.httpserver.HttpHandler {

  protected final HttpContext context;

  public HttpHandler(
      final HttpServer server,
      final String path,
      final HttpMethod method,
      final Authenticator authenticator
  ) {
    context = server.createContext(path, this);
    context.getFilters().add(new JsonFilter(method.name()));
    if (authenticator != null) {
      context.setAuthenticator(authenticator);
    }
  }

  public final void handle(final HttpExchange exchange) throws IOException {
    exchange.getResponseHeaders().set(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
    doHandle(exchange);
  }

  protected abstract void doHandle(HttpExchange exchange) throws IOException;
}
