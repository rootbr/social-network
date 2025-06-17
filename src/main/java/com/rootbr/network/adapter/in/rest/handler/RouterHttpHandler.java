package com.rootbr.network.adapter.in.rest.handler;

import com.rootbr.network.adapter.in.rest.server.HttpMethod;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.adapter.in.rest.server.Router;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

public class RouterHttpHandler implements HttpHandler {
  public static final String NOT_FOUND_RESPONSE = "<h1>404 Not Found</h1>No context found for request";

  private final Map<HttpMethod, Router> routers;

  public RouterHttpHandler() {
    routers = new EnumMap<>(HttpMethod.class);
    for (final HttpMethod value : HttpMethod.values()) {
      routers.put(value, new Router());
    }
  }

  public void registerRoute(final HttpMethod method, final String path, final RestHandler handler) {
    routers.get(method).registerRoute(path, handler);
  }

  @Override
  public void handle(final HttpExchange exchange) throws IOException {
    final HttpMethod method = HttpMethod.of(exchange.getRequestMethod());
    final String path = exchange.getRequestURI().getPath();
    final boolean routeHandled = routers.get(method).route(path, exchange);
    if (!routeHandled) {
      exchange.sendResponseHeaders(404, NOT_FOUND_RESPONSE.length());
      exchange.getResponseBody().write(NOT_FOUND_RESPONSE.getBytes());
      exchange.getResponseBody().flush();
      exchange.close();
    }
  }
}
