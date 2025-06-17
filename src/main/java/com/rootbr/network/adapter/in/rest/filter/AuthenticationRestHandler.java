package com.rootbr.network.adapter.in.rest.filter;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.TokenService;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AuthenticationRestHandler implements RestHandler {

  private final TokenService tokenService;
  private final SocialNetworkApplication application;
  private final RestHandler delegate;

  public AuthenticationRestHandler(final TokenService tokenService,
      final SocialNetworkApplication application, final RestHandler delegate) {
    this.tokenService = tokenService;
    this.application = application;
    this.delegate = delegate;
  }

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal p, final SocialNetworkApplication application, final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters) throws IOException {
    final String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
    if (authHeader == null) {
      consumeInputAndReject(exchange);
      return;
    }
    final int sp = authHeader.indexOf(' ');
    if (sp == -1 || !authHeader.substring(0, sp).equals("Bearer")) {
      consumeInputAndReject(exchange);
      return;
    }
    final String token = authHeader.substring(sp + 1);
    final Principal principal = this.tokenService.createPrincipal(token, this.application::principal);
    if (principal == null) {
      consumeInputAndReject(exchange);
      return;
    }
    this.delegate.handle(exchange, factory, principal, this.application, pathVariables, queryParameters);
  }


  private final byte[] b = new byte[4096];

  private void consumeInputAndReject(final HttpExchange exchange) throws IOException {
    final InputStream i = exchange.getRequestBody();
    while (i.read(b) != -1) ;
    i.close();
    exchange.sendResponseHeaders(401, -1);
  }
}
