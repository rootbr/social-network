package com.rootbr.network.adapter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.rootbr.network.adapter.out.db.UserPortImpl;
import com.rootbr.network.application.AuthenticationService;
import com.rootbr.network.application.Principal;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.domain.engine.UserVisitor;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.Executors;
import javax.sql.DataSource;

public class SimplApp {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";
  public static final long START_APP = System.nanoTime();

  public static void main(String[] args) throws IOException {

    final JsonFactory factory = new JsonFactory();
    final AuthenticationService authenticationService = new BCryptJwtAuthenticationService("yourStrongSecretKeyWithAtLeast32Characters", 60);

    final HikariConfig config = new HikariConfig();
    config.setJdbcUrl("jdbc:postgresql://localhost:5432/db");
    config.setUsername("postgres");
    config.setPassword("postgres");
    config.setMaximumPoolSize(10);
    config.setMinimumIdle(2);
    config.setIdleTimeout(30000);
    config.setConnectionTimeout(20000);
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    final SocialNetworkApplication socialNetworkApplication = new SocialNetworkApplication(
        new UserPortImpl(
            new HikariDataSource(config)
        )
    );
    final JwtAuthenticator authenticator = new JwtAuthenticator(socialNetworkApplication);
    final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

    server.createContext("/user/register", exchange -> {
      if (!"POST".equals(exchange.getRequestMethod())
          && !exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE)
          .contains(CONTENT_TYPE_APPLICATION_JSON)) {
        return;
      }

      String firstName = null, lastName = null, biography = null, city = null, password = null;
      LocalDate birthdate = null;
      try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
        JsonToken jsonToken;
        while ((jsonToken = parser.nextToken()) != null) {
          if (jsonToken == JsonToken.FIELD_NAME) {
            final String fieldName = parser.getText();
            parser.nextToken();
            switch (fieldName) {
              case "first_name":
                firstName = parser.getValueAsString();
                break;
              case "second_name":
                lastName = parser.getValueAsString();
                break;
              case "birthdate":
                birthdate = LocalDate.parse(parser.getValueAsString());
                break;
              case "biography":
                biography = parser.getValueAsString();
                break;
              case "city":
                city = parser.getValueAsString();
                break;
              case "password":
                password = parser.getValueAsString();
                break;
              default:
                throw new IllegalArgumentException("Unknown field name: " + fieldName);
            }
          }
        }
      }

      final String userId = UUID.randomUUID().toString();
      java.security.Principal principal = socialNetworkApplication.createPrincipal(userId, password);
      principal.registerUser(userId, firstName, lastName, city, birthdate, biography);

      exchange.getResponseHeaders().set(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
      exchange.sendResponseHeaders(200, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();
        generator.writeStringField("userId", userId);
        generator.writeEndObject();
      }
    });

    server.createContext("/login", exchange -> {
      if (!"POST".equals(exchange.getRequestMethod())
          && !exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE)
          .contains(CONTENT_TYPE_APPLICATION_JSON)) {
        return;
      }
      String id = null, password = null;
      try (final JsonParser parser = factory.createParser(exchange.getRequestBody())) {
        JsonToken jsonToken;
        while ((jsonToken = parser.nextToken()) != null) {
          if (jsonToken == JsonToken.FIELD_NAME && "id".equals(parser.getText())) {
            parser.nextToken();
            id = parser.getValueAsString();
          } else if (jsonToken == JsonToken.FIELD_NAME && "password".equals(parser.getText())) {
            parser.nextToken();
            password = parser.getValueAsString();
          }
        }
      }
      if (id == null || password == null) {
        exchange.sendResponseHeaders(400, -1);
        return;
      }

      final String userId = UUID.randomUUID().toString();
      java.security.Principal principal = socialNetworkApplication.login(userId, password);

      if (principal == null) {
        exchange.sendResponseHeaders(401, -1);
        return;
      }

      exchange.getResponseHeaders().set(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
      exchange.sendResponseHeaders(200, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();
        generator.writeStringField("token", principal.generateToken());
        generator.writeEndObject();
      }
    });

    server.createContext("/user/", exchange -> {
      final Principal principal = ((Principal) exchange.getPrincipal());
      final String userId = exchange.getRequestURI().getPath().substring(7);
      exchange.getResponseHeaders().set(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
      exchange.sendResponseHeaders(200, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();
        principal.readUser(userId, new UserVisitor() {
          void visitUser(String id) throws IOException {
            generator.writeStringField("userId", id);
          }
        });
        generator.writeEndObject();
      }
    }).setAuthenticator(authenticator);

    server.start();
    System.out.println("Server started on port 8080 with virtual threads by "
        + (System.nanoTime() - START_APP) / 1000000 + " ms");
  }


  private static class JwtAuthenticator extends Authenticator {

    public static final Retry RETRY = new Retry(401);
    public static final Failure FAILURE = new Failure(401);

    private final SocialNetworkApplication socialNetworkApplication;

    private JwtAuthenticator(final SocialNetworkApplication socialNetworkApplication) {
      this.socialNetworkApplication = socialNetworkApplication;
    }

    public Result authenticate(final HttpExchange exchange) {
      final Headers headers = exchange.getRequestHeaders();
      final String authHeader = headers.getFirst("Authorization");
      if (authHeader == null) {
        return RETRY;
      }
      final int sp = authHeader.indexOf(' ');
      if (sp == -1 || !authHeader.substring(0, sp).equals("Bearer")) {
        return FAILURE;
      }
      final java.security.Principal principal = socialNetworkApplication.login(authHeader.substring(sp + 1));
      if (principal == null) {
        return FAILURE;
      }
      return new Authenticator.Success(new HttpPrincipal(principal.getName(), ""));
    }
  }
}
