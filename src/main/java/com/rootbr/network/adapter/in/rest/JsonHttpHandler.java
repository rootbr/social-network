package com.rootbr.network.adapter.in.rest;

import static com.rootbr.network.adapter.in.rest.JsonFilter.CONTENT_TYPE_APPLICATION_JSON;
import static com.rootbr.network.adapter.in.rest.JsonFilter.HEADER_CONTENT_TYPE;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.util.List;

public abstract class JsonHttpHandler implements com.sun.net.httpserver.HttpHandler {

  protected final HttpContext context;
  protected final JsonFactory jsonFactory;
  protected final SocialNetworkApplication application;

  public JsonHttpHandler(
      final HttpServer server,
      final String path,
      final HttpMethod method,
      final Authenticator authenticator,
      final JsonFactory jsonFactory,
      final SocialNetworkApplication application,
      final List<Filter> filters
  ) {
    this.jsonFactory = jsonFactory;
    this.application = application;
    context = server.createContext(path, this);
    context.getFilters().addAll(filters);
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
