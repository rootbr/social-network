package com.rootbr.network.adapter.in.rest;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class EmptyFilter extends Filter {

  @Override
  public void doFilter(final HttpExchange exchange, final Chain chain) throws IOException {
    chain.doFilter(exchange);
  }

  @Override
  public String description() {
    return "EMPTY";
  }
}
