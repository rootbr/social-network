package com.rootbr.network.adapter.in.rest.server;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Router {

  private final Node root = new Node();
  private final Map<String, RestHandler> staticPath = new HashMap<>();

  public boolean route(final String path, final HttpExchange exchange) throws IOException {
    final RestHandler handler = staticPath.get(path);
    if (handler != null) {
      handler.handle(exchange, null, null, null, new String[0]);
      return true;
    }
    final String[] parts = path.split("/");
    Node node = root;
    String[] pathVariables = new String[1];
    
    for (final String part : parts) {
      final Node tmp = node.nodes.get(part);
      if (tmp != null) {
        node = tmp;
      } else {
        if (node.variable != null) {
          if (pathVariables[pathVariables.length - 1] == null) {
            pathVariables[pathVariables.length - 1] = part;
          } else {
            pathVariables = Arrays.copyOf(pathVariables, pathVariables.length + 1);
            pathVariables[pathVariables.length - 1] = part;
          }
          node = node.variable;
        } else {
          return false;
        }
      }
    }
    if (node.handler != null) {
      final String[] finalPathVariables = pathVariables[0] == null ? new String[0] : pathVariables;
      node.handler.handle(exchange, null, null, null, finalPathVariables);
      return true;
    } else {
      return false;
    }
  }

  public void registerRoute(final String path, final RestHandler handler) {
    if (!path.contains("${")) {
      staticPath.put(path, handler);
      return;
    }
    final String[] parts = path.split("/");
    Node node = root;
    for (final String part : parts) {
      final boolean isVariable = part.startsWith("${") && part.endsWith("}");
      if (isVariable) {
        if (node.variable != null) {
          node = node.variable;
        } else {
          node.variable = new Node();
          node = node.variable;
        }
      } else {
        node = node.nodes.computeIfAbsent(part, k -> new Node());
      }
    }
    node.handler = handler;
  }

  private static class Node {

    private final Map<String, Node> nodes = new HashMap<>();
    private Node variable;
    private RestHandler handler;
  }
}




















