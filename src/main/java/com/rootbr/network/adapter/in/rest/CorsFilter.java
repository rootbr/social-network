package com.rootbr.network.adapter.in.rest;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

public class CorsFilter extends Filter {

  @Override
  public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
    exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    exchange.getResponseHeaders().add("Access-Control-Max-Age", "3600");
    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
      exchange.sendResponseHeaders(204, -1);
      exchange.close();
      return;
    }
    chain.doFilter(exchange);
  }

  @Override
  public String description() {
    return "CORS";
  }
}
