package com.rootbr.network.adapter.in.rest.filter;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ApplicationJsonRestHandler implements RestHandler {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HEADER_ACCEPT = "Accept";
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

  private final byte[] b = new byte[4096];
  private final JsonFactory jsonFactory;
  private final RestHandler handler;


  public ApplicationJsonRestHandler(final JsonFactory jsonFactory, final RestHandler handler) {
    this.jsonFactory = jsonFactory;
    this.handler = handler;
  }

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory jsonFactory,
      final Principal principal, final SocialNetworkApplication application) throws IOException {
    final List<String> headersAccept = exchange.getRequestHeaders().get(HEADER_ACCEPT);
    final boolean notAcceptable = headersAccept == null || headersAccept.isEmpty() || !headersAccept.getFirst().contains(CONTENT_TYPE_APPLICATION_JSON);
    if (!notAcceptable) {
      exchange.getResponseHeaders().set(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
      handler.handle(exchange, this.jsonFactory, principal, application);
    } else {
      final InputStream i = exchange.getRequestBody();
      while (i.read(b) != -1) ;
      i.close();
      exchange.sendResponseHeaders(406, -1);
    }
  }
}
