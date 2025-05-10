package com.rootbr.network.adapter.in.rest;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

public class JsonFilter extends Filter {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
  private final String method;

  public JsonFilter(final String method) {
    this.method = method;
  }

  @Override
  public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
    final List<String> headers = exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE);
    if (method.equals(exchange.getRequestMethod()) && headers != null && headers.contains(
        CONTENT_TYPE_APPLICATION_JSON)) {
      chain.doFilter(exchange);
    } else {
      exchange.sendResponseHeaders(400, -1);
    }
  }

  @Override
  public String description() {
    return method + " application/json";
  }
}
