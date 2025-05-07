package com.rootbr.network.adapter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class SimplApp {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

  public static void main(String[] args) throws IOException {
    final JsonFactory factory = new JsonFactory();
    final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    server.createContext("/login", exchange -> {
      if (!"POST".equals(exchange.getRequestMethod())
          && !exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).contains(CONTENT_TYPE_APPLICATION_JSON)) {
        return;
      }

      exchange.getResponseHeaders().set(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
      exchange.sendResponseHeaders(200, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();
        generator.writeStringField("brand", "BMW");
        generator.writeNumberField("doors", 5);
        generator.writeEndObject();
      }
    });
    server.start();
    System.out.println("Server started on port 8080 with virtual threads");
  }

}
