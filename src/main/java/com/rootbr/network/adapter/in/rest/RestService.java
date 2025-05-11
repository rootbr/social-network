package com.rootbr.network.adapter.in.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.handler.GetUserById;
import com.rootbr.network.adapter.in.rest.handler.PostApiUserRegister;
import com.rootbr.network.adapter.in.rest.handler.PostLogin;
import com.rootbr.network.adapter.in.rest.handler.PostUserRegister;
import com.rootbr.network.adapter.out.db.PrincipalPortImpl;
import com.rootbr.network.adapter.out.db.UserPortImpl;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class RestService {
  private static final String API_CONTEXT = "/api";
  private static final Map<String, String> MIME_TYPES = new HashMap<>();

  static {
    // Инициализация карты MIME-типов
    MIME_TYPES.put("html", "text/html");
    MIME_TYPES.put("js", "text/javascript");
    MIME_TYPES.put("css", "text/css");
    MIME_TYPES.put("json", "application/json");
    MIME_TYPES.put("png", "image/png");
    MIME_TYPES.put("jpg", "image/jpeg");
    MIME_TYPES.put("jpeg", "image/jpeg");
    MIME_TYPES.put("gif", "image/gif");
    MIME_TYPES.put("svg", "image/svg+xml");
    MIME_TYPES.put("ico", "image/x-icon");
    MIME_TYPES.put("txt", "text/plain");
  }
  public static final long START_APP = System.nanoTime();

  public static void main(final String[] args) throws IOException {
    final ApplicationConfiguration config = new ApplicationConfiguration("config.yml");
    final HikariDataSource dataSource = new HikariDataSource(
        new HikariConfig(config.properties("datasource"))
    );
    final SocialNetworkApplication application = new SocialNetworkApplication(
        new BCryptJwtAuthenticationService(
            new PrincipalPortImpl(dataSource), config.properties("auth")
        ),
        new UserPortImpl(dataSource)
    );
    final HttpServer server = HttpServer.create(
        new InetSocketAddress(config.getInt("server.port")),
        config.getInt("server.backlog")
    );
    server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

    List<Filter> filters = Arrays.asList(
        Arrays.asList(args).contains("--debug") ? new CorsFilter() {} : new EmptyFilter()
    );

    final JwtAuthenticator authenticator = new JwtAuthenticator(application);
    final JsonFactory factory = new JsonFactory();

    new PostApiUserRegister(server, factory, application, filters);
    new PostUserRegister(server, factory, application, filters);
    new PostLogin(server, factory, application, filters);
    new GetUserById(server, factory, application, authenticator, filters);
    server.createContext("/", new StaticFileHandler())
        .getFilters().addAll(filters);

    server.start();
    System.out.println("Server started on port " + server.getAddress().getPort() + " with virtual threads by " + (System.nanoTime() - START_APP) / 1000000 + " ms");
  }

  static class StaticFileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      URI requestURI = exchange.getRequestURI();
      String path = requestURI.getPath();

      // Проверяем, если это запрос API - пропускаем
      if (path.startsWith(API_CONTEXT)) {
        exchange.sendResponseHeaders(404, -1);
        exchange.close();
        return;
      }

      System.out.println("Static file request: " + path);

      // Очищаем путь от начального слеша
      if (path.startsWith("/")) {
        path = path.substring(1);
      }

      // Если путь пустой или запрашивается директория, используем index.html
      if (path.isEmpty() || path.endsWith("/")) {
        path = "index.html";
      }

      // Проверяем, соответствует ли путь реальному файлу
      // Если нет, предполагаем, что это клиентский роутинг и возвращаем index.html
      String fullPath = "static/" + path;
      InputStream resourceStream = RestService.class.getClassLoader().getResourceAsStream(fullPath);

      if (resourceStream == null) {
        // Файл не найден, возможно это клиентский роутинг React
        // Возвращаем index.html
        resourceStream = RestService.class.getClassLoader().getResourceAsStream("static/index.html");
        if (resourceStream == null) {
          // Если даже index.html не найден, отправляем 404
          String response = "404 Not Found";
          sendResponse(exchange, 404, response, "text/plain");
          return;
        }
        path = "index.html";
      }

      // Определение MIME-типа
      String mimeType = getMimeType(path);

      // Отправка файла
      byte[] bytes = readAllBytes(resourceStream);
      sendResponse(exchange, 200, bytes, mimeType);
    }

    // Определяем MIME-тип по расширению файла
    private String getMimeType(String path) {
      int lastDot = path.lastIndexOf('.');
      if (lastDot >= 0) {
        String extension = path.substring(lastDot + 1).toLowerCase();
        return MIME_TYPES.getOrDefault(extension, "application/octet-stream");
      }
      return "application/octet-stream";
    }

    // Чтение всех байтов из InputStream
    private byte[] readAllBytes(InputStream is) throws IOException {
      return is.readAllBytes();
    }
  }

  private static class JwtAuthenticator extends Authenticator {

    public static final Retry RETRY = new Retry(401);
    public static final Failure FAILURE = new Failure(401);

    private final SocialNetworkApplication socialNetworkApplication;

    private JwtAuthenticator(final SocialNetworkApplication socialNetworkApplication) {
      this.socialNetworkApplication = socialNetworkApplication;
    }

    @Override
    public Result authenticate(final HttpExchange exchange) {
      final String authHeader = exchange.getRequestHeaders().getFirst("Authorization");
      if (authHeader == null) {
        return RETRY;
      }
      final int sp = authHeader.indexOf(' ');
      if (sp == -1 || !authHeader.substring(0, sp).equals("Bearer")) {
        return FAILURE;
      }
      final HttpPrincipal principal = socialNetworkApplication.authorize(
          authHeader.substring(sp + 1));
      if (principal == null) {
        return FAILURE;
      }
      return new Authenticator.Success(principal);
    }
  }

  // Метод для отправки ответа в виде строки
  private static void sendResponse(HttpExchange exchange, int statusCode, String response, String contentType) throws IOException {
    sendResponse(exchange, statusCode, response.getBytes(), contentType);
  }

  // Метод для отправки ответа в виде байтов
  private static void sendResponse(HttpExchange exchange, int statusCode, byte[] response, String contentType) throws IOException {
    Headers headers = exchange.getResponseHeaders();
    headers.set("Content-Type", contentType);

    // Добавляем CORS-заголовки для разработки
    headers.set("Access-Control-Allow-Origin", "*");
    headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

    if (exchange.getRequestMethod().equals("OPTIONS")) {
      // Ответ на preflight-запрос
      exchange.sendResponseHeaders(204, -1);
      exchange.close();
      return;
    }

    exchange.sendResponseHeaders(statusCode, response.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(response);
    }
  }

  // Обработка CORS в HttpServer
  private void handleCORS(HttpExchange exchange) {
    Headers headers = exchange.getResponseHeaders();
    headers.set("Access-Control-Allow-Origin", "*"); // В продакшене используйте конкретный домен
    headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    headers.set("Access-Control-Max-Age", "3600");

    if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
      try {
        exchange.sendResponseHeaders(204, -1);
      } catch (IOException e) {
        e.printStackTrace();
      }
      exchange.close();
    }
  }
}
