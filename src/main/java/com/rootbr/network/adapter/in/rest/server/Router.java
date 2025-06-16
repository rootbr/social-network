package com.rootbr.network.adapter.in.rest.server;

import java.util.HashMap;
import java.util.Map;

public class Router {

  private final Node root = new Node();
  private final Map<String, RestHandler> staticPath = new HashMap<>();

  public RestHandler route(final String path) {
    final RestHandler handler = staticPath.get(path);
    if (handler != null) {
      return handler;
    }
    final String[] parts = path.split("/");
    Node node = root;
    for (final String part : parts) {
      final Node tmp = node.nodes.get(part);
      if (tmp != null) {
        node = tmp;
      } else {
        if (node.variable != null) {
          node = node.variable;
        } else {
          return null;
        }
      }
    }
    if (node.handler != null) {
      return node.handler;
    } else {
      return null;
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




















