package com.rootbr.network.adapter.in.rest.server;

import com.rootbr.network.adapter.in.rest.filter.CorsFilter;
import com.rootbr.network.adapter.in.rest.handler.RouterHttpHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

public class Server {

  public static final long START_APP = System.nanoTime();
  private final HttpServer server;
  private final RouterHttpHandler router = new RouterHttpHandler();

  public Server(final Properties properties, final ExecutorService executor) throws IOException {
    this.server = HttpServer.create(
        new InetSocketAddress(Integer.parseInt(properties.getProperty("port"))),
        Integer.parseInt(properties.getProperty("backlog"))
    );
    this.server.setExecutor(executor);
    final HttpContext apiContext = server.createContext("/", router);
    if (System.getProperty("debug") != null) {
      apiContext.getFilters().add(new CorsFilter());
    }
  }

  public Server registerHttpHandler(final HttpMethod method, final String path, final RestHandler handler) {
    router.registerRoute(method, path, handler);
    return this;
  }

  public void start() {
    server.start();
    System.out.println(
        "Server started on port " + server.getAddress().getPort() + " with virtual threads by "
            + (System.nanoTime() - START_APP) / 1000000 + " ms");
  }
}
