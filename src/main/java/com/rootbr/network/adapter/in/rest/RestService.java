package com.rootbr.network.adapter.in.rest;

import static com.rootbr.network.adapter.in.rest.server.HttpMethod.GET;
import static com.rootbr.network.adapter.in.rest.server.HttpMethod.POST;
import static com.rootbr.network.adapter.in.rest.server.HttpMethod.PUT;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.filter.AnonymousRestHandler;
import com.rootbr.network.adapter.in.rest.filter.ApplicationJsonRestHandler;
import com.rootbr.network.adapter.in.rest.filter.AuthenticationRestHandler;
import com.rootbr.network.adapter.in.rest.filter.ErrorInterceptor;
import com.rootbr.network.adapter.in.rest.handler.DialogListHandler;
import com.rootbr.network.adapter.in.rest.handler.DialogSendHandler;
import com.rootbr.network.adapter.in.rest.handler.FriendDeleteHandler;
import com.rootbr.network.adapter.in.rest.handler.FriendSetHandler;
import com.rootbr.network.adapter.in.rest.handler.GetUserHandler;
import com.rootbr.network.adapter.in.rest.handler.LoginHandler;
import com.rootbr.network.adapter.in.rest.handler.PostCreateHandler;
import com.rootbr.network.adapter.in.rest.handler.PostDeleteHandler;
import com.rootbr.network.adapter.in.rest.handler.PostFeedHandler;
import com.rootbr.network.adapter.in.rest.handler.PostGetHandler;
import com.rootbr.network.adapter.in.rest.handler.PostUpdateHandler;
import com.rootbr.network.adapter.in.rest.handler.UserRegisterHandler;
import com.rootbr.network.adapter.in.rest.handler.UserSearchHandler;
import com.rootbr.network.adapter.in.rest.server.JwtService;
import com.rootbr.network.adapter.in.rest.server.Server;
import com.rootbr.network.adapter.out.db.PrincipalPortImpl;
import com.rootbr.network.adapter.out.db.UserPortImpl;
import com.rootbr.network.application.SocialNetworkApplication;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RestService {

  public static void main(final String[] args) throws IOException, InterruptedException {
    final ApplicationConfiguration config = new ApplicationConfiguration("config.yml");
    final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
    final JsonFactory jsonFactory = new JsonFactory();
    final TokenService tokenService = new JwtService(config.properties("auth"));
    final SocialNetworkApplication application = new SocialNetworkApplication(
        jsonFactory,
        new HikariDataSource(
            new HikariConfig(config.properties("dataSource"))
        ),
        new BCryptPasswordEncoder(config.properties("auth")),
        new UserPortImpl(),
        new PrincipalPortImpl()
    );
    new Server(config.properties("server"), executor)
        .registerHttpHandler(
            POST, "/login",
            new ErrorInterceptor(
                new AnonymousRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new LoginHandler(tokenService)
                    )
                )
            )
        )
        .registerHttpHandler(
            POST, "/user/register",
            new ErrorInterceptor(
                new AnonymousRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new UserRegisterHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            GET, "/user/get/${id}",
            new ErrorInterceptor(
                new AnonymousRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new GetUserHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            GET, "/user/search",
            new ErrorInterceptor(
                new AnonymousRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new UserSearchHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            PUT, "/friend/set/${user_id}",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new FriendSetHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            PUT, "/friend/delete/${user_id}",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new FriendDeleteHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            POST, "/post/create",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new PostCreateHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            PUT, "/post/update",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new PostUpdateHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            PUT, "/post/delete/${id}",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new PostDeleteHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            GET, "/post/get/${id}",
            new ErrorInterceptor(
                new AnonymousRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new PostGetHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            GET, "/post/feed",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new PostFeedHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            POST, "/dialog/${user_id}/send",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new DialogSendHandler()
                    )
                )
            )
        )
        .registerHttpHandler(
            GET, "/dialog/${user_id}/list",
            new ErrorInterceptor(
                new AuthenticationRestHandler(
                    tokenService,
                    application,
                    new ApplicationJsonRestHandler(
                        jsonFactory,
                        new DialogListHandler()
                    )
                )
            )
        )
        .start();
  }
}