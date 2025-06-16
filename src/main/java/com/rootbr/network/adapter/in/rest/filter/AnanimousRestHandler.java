package com.rootbr.network.adapter.in.rest.filter;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.legalai.application.LegalAiApplication;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public class AnanimousRestHandler implements RestHandler {

  private final SocialNetworkApplication application;
  private final RestHandler delegate;

  public AnanimousRestHandler(final SocialNetworkApplication application, final RestHandler delegate) {
    this.application = application;
    this.delegate = delegate;
  }

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application) throws IOException {
    delegate.handle(exchange, factory, this.application.anonymous(), this.application);
  }
}
