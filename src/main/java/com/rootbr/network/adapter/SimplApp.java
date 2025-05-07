package com.rootbr.network.adapter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.sun.net.httpserver.HttpServer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.Executors;

public class SimplApp {

  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

  public static void main(String[] args) throws IOException {
    final JsonFactory factory = new JsonFactory();
    final Auth auth = new Auth("yourStrongSecretKeyWithAtLeast32Characters", 60);
    final HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
    server.createContext("/login", exchange -> {
      if (!"POST".equals(exchange.getRequestMethod())
          && !exchange.getRequestHeaders().get(HEADER_CONTENT_TYPE).contains(CONTENT_TYPE_APPLICATION_JSON)) {
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

      final String hashedm7Password = auth.cryptPassword(password);

      exchange.getResponseHeaders().set(HEADER_CONTENT_TYPE, CONTENT_TYPE_APPLICATION_JSON);
      exchange.sendResponseHeaders(200, 0);
      try (final JsonGenerator generator = factory.createGenerator(exchange.getResponseBody())) {
        generator.writeStartObject();
        generator.writeStringField("token", auth.generateToken(id));
        generator.writeEndObject();
      }
    });
    server.start();
    System.out.println("Server started on port 8080 with virtual threads");
  }

  //        final String path = request.getRequestURI();
  //        if (request.getMethod().equals("POST") && (path.equals("/login") || path.equals("/user/register"))) {
  //          filterChain.doFilter(request, response);
  //          return;
  //        }
  //
  //        final String authHeader = request.getHeader("Authorization");
  //        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
  //          response.setStatus(HttpStatus.UNAUTHORIZED.value());
  //          return;
  //        }
  //        final String jwt = authHeader.substring(7);
  //        if (!authService.isValidToken(jwt)) {
  //          response.setStatus(HttpStatus.UNAUTHORIZED.value());
  //          return;
  //        }
  //        request.setAttribute(ATTRIBUTE_USER_ID, authService.getUserIdFromToken(jwt));
  //        filterChain.doFilter(request, response);


  static class Auth {
    private final long tokenValidityInSeconds;
    private final Key key;
    private Hasher hasher = BCrypt.withDefaults();;
    private Verifyer verifyer = BCrypt.verifyer();

    Auth(final String secretKey, final long tokenValidityInSeconds) {
      this.tokenValidityInSeconds = tokenValidityInSeconds;
      this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));;
    }

    public String generateToken(final String id) {
      final Date now = new Date();
      final Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);
      return Jwts.builder()
          .subject(id)
          .issuedAt(now)
          .expiration(validity)
          .signWith(key)
          .compact();
    }

    public String cryptPassword(final String password) {
      return hasher.hashToString(12, password.toCharArray());
    }

    public boolean validatePassword(final String password, final String hashedPassword) {
      return verifyer.verify(password.toCharArray(), hashedPassword).verified;
    }
  }
}
