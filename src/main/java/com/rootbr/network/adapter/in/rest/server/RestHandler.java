package com.rootbr.network.adapter.in.rest.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public interface RestHandler {

  void handle(final HttpExchange exchange, final JsonFactory factory, final Principal principal, final SocialNetworkApplication application)
      throws IOException;

}
