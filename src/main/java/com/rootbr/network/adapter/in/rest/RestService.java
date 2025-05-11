package com.rootbr.network.adapter.in.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.handler.GetUserById;
import com.rootbr.network.adapter.in.rest.handler.PostLogin;
import com.rootbr.network.adapter.in.rest.handler.PostUserRegister;
import com.rootbr.network.adapter.out.db.PrincipalPortImpl;
import com.rootbr.network.adapter.out.db.UserPortImpl;
import com.rootbr.network.application.SocialNetworkApplication;
import com.sun.net.httpserver.Authenticator;
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

    final JwtAuthenticator authenticator = new JwtAuthenticator(application);
    final JsonFactory factory = new JsonFactory();

    new PostUserRegister(server, factory, application);
    new PostLogin(server, factory, application);
    new GetUserById(server, factory, application, authenticator);

    server.start();
    System.out.println("Server started on port " + server.getAddress().getPort() + " with virtual threads by " + (System.nanoTime() - START_APP) / 1000000 + " ms");
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
}
