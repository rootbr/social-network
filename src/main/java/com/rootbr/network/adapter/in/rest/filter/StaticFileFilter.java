package com.rootbr.network.adapter.in.rest.filter;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StaticFileFilter implements HttpHandler {

  private static final String STATIC_DIR = "static/";
  private static final String INDEX_HTML = "index.html";
  private static final String DEFAULT_MIME_TYPE = "application/octet-stream";

  private static final Map<String, String> MIME_TYPES;

  static {
    final Map<String, String> mimeMap = new HashMap<>();
    mimeMap.put("html", "text/html");
    mimeMap.put("js", "text/javascript");
    mimeMap.put("css", "text/css");
    mimeMap.put("json", "application/json");
    mimeMap.put("png", "image/png");
    mimeMap.put("jpg", "image/jpeg");
    mimeMap.put("jpeg", "image/jpeg");
    mimeMap.put("gif", "image/gif");
    mimeMap.put("svg", "image/svg+xml");
    mimeMap.put("ico", "image/x-icon");
    mimeMap.put("txt", "text/plain");
    MIME_TYPES = Collections.unmodifiableMap(mimeMap);
  }

  @Override
  public void handle(final HttpExchange exchange) throws IOException {
    final String path = exchange.getRequestURI().getPath();
    final String requestedPath = getNormalizedPath(path);
    serveFileOrIndexHtml(exchange, requestedPath);
  }

  private String getNormalizedPath(String path) {
    if (path.startsWith("/")) {
      path = path.substring(1);
    }
    if (path.isEmpty() || path.endsWith("/")) {
      path = INDEX_HTML;
    }
    return path;
  }

  private void serveFileOrIndexHtml(final HttpExchange exchange, final String path) throws IOException {
    final String fullPath = STATIC_DIR + path;
    try (final InputStream resourceStream = getResourceStream(fullPath)) {
      if (resourceStream != null) {
        serveResource(exchange, resourceStream, path);
        return;
      }
      try (final InputStream indexStream = getResourceStream(STATIC_DIR + INDEX_HTML)) {
        if (indexStream != null) {
          serveResource(exchange, indexStream, INDEX_HTML);
          return;
        }
        sendErrorResponse(exchange, 404, "Not Found");
      }
    }
  }

  private InputStream getResourceStream(final String path) {
    return StaticFileFilter.class.getClassLoader().getResourceAsStream(path);
  }

  private void serveResource(final HttpExchange exchange, final InputStream resourceStream,
      final String path) throws IOException {
    exchange.getResponseHeaders().set("Content-Type", getMimeType(path));
    exchange.sendResponseHeaders(200, 0);
    try (final OutputStream os = exchange.getResponseBody()) {
      resourceStream.transferTo(os);
    }
  }

  private String getMimeType(final String path) {
    final int lastDot = path.lastIndexOf('.');
    if (lastDot >= 0) {
      final String extension = path.substring(lastDot + 1).toLowerCase();
      return MIME_TYPES.getOrDefault(extension, DEFAULT_MIME_TYPE);
    }
    return DEFAULT_MIME_TYPE;
  }

  private void sendErrorResponse(final HttpExchange exchange, final int statusCode,
      final String message)
      throws IOException {
    final byte[] response = message.getBytes();
    exchange.getResponseHeaders().set("Content-Type", "text/plain");
    exchange.sendResponseHeaders(statusCode, response.length);
    try (final OutputStream os = exchange.getResponseBody()) {
      os.write(response);
    }
  }
}