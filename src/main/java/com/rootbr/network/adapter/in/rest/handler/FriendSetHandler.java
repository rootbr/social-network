package com.rootbr.network.adapter.in.rest.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.server.RestHandler;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FriendSetHandler implements RestHandler {

  @Override
  public void handle(final HttpExchange exchange, final JsonFactory factory,
      final Principal principal, final SocialNetworkApplication application,
      final String[] pathVariables, final Function<HttpExchange, Map<String, List<String>>> queryParameters)
      throws IOException {
    final String path = exchange.getRequestURI().getPath();
    final String friendUserId = extractPathVariable(path, "/friend/set/");

    if (friendUserId == null) {
      exchange.sendResponseHeaders(400, -1);
      return;
    }

    principal.execute(application.addFriendCommand(friendUserId));
    exchange.sendResponseHeaders(200, -1);
  }

  private String extractPathVariable(final String path, final String prefix) {
    if (path.startsWith(prefix)) {
      return path.substring(prefix.length());
    }
    return null;
  }
}