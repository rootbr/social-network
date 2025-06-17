package com.rootbr.network.adapter.in.rest.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface RestHandler {

  void handle(final HttpExchange exchange, final JsonFactory factory, final Principal principal, final SocialNetworkApplication application, final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException;

}
