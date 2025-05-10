package com.rootbr.network.adapter.in.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.BCryptJwtAuthenticationService;
import com.rootbr.network.adapter.in.rest.handler.GetUserById;
import com.rootbr.network.adapter.in.rest.handler.PostLogin;
import com.rootbr.network.adapter.in.rest.handler.PostUserRegister;
import com.rootbr.network.adapter.out.db.UserPortImpl;
import com.rootbr.network.application.AuthenticationService;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import com.sun.net.httpserver.HttpServer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class RestService {


  public static final long START_APP = System.nanoTime();

  public static void main(String[] args) throws IOException {

    final JsonFactory factory = new JsonFactory();
    final AuthenticationService authenticationService = new BCryptJwtAuthenticationService(
        "yourStrongSecretKeyWithAtLeast32Characters", 60);

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

    new PostUserRegister(server, factory, socialNetworkApplication);
    new PostLogin(server, factory, socialNetworkApplication);
    new GetUserById(server, factory, socialNetworkApplication, authenticator);

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

    @Override
    public Result authenticate(final HttpExchange exchange) {
      System.out.println("authenticate");
      final Headers headers = exchange.getRequestHeaders();
      final String authHeader = headers.getFirst("Authorization");
      if (authHeader == null) {
        return RETRY;
      }
      final int sp = authHeader.indexOf(' ');
      if (sp == -1 || !authHeader.substring(0, sp).equals("Bearer")) {
        return FAILURE;
      }
//      final java.security.Principal principal = socialNetworkApplication.login(authHeader.substring(sp + 1));
//      if (principal == null) {
//        return FAILURE;
//      }
//      return new Authenticator.Success(new HttpPrincipal(principal.getName(), ""));
      return new Authenticator.Success(new HttpPrincipal("principal.getName()", ""));
    }
  }

}
