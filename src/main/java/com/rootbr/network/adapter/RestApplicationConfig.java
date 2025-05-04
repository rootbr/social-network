package com.rootbr.network.adapter;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import com.rootbr.network.adapter.in.rest.AuthService;
import com.rootbr.network.adapter.out.db.UserPortImpl;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.domain.port.db.UserPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class RestApplicationConfig {

  public static final String ATTRIBUTE_USER_ID = "userId";

  @Bean
  public DSLContext dslContext(DataSource dataSource) {
    return DSL.using(new TransactionAwareDataSourceProxy(dataSource), SQLDialect.POSTGRES);
  }

  @Bean
  public UserPort userPort(DSLContext dsl) {
    return new UserPortImpl(dsl);
  }

  @Bean
  public SocialNetworkApplication socialNetworkApplication(UserPort userPort) {
    return new SocialNetworkApplication(userPort, new BCryptPasswordEncoder());
  }

  @Bean
  public OncePerRequestFilter authenticationFilter(AuthService authService) {
    return new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String path = request.getRequestURI();
        if (request.getMethod().equals("POST") && (path.equals("/login") || path.equals("/user/register"))) {
          filterChain.doFilter(request, response);
          return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return;
        }
        final String jwt = authHeader.substring(7);
        if (!authService.isValidToken(jwt)) {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return;
        }
        request.setAttribute(ATTRIBUTE_USER_ID, authService.getUserIdFromToken(jwt));
        filterChain.doFilter(request, response);
      }
    };
  }

  public static String getUserId() {
    return (String) RequestContextHolder.getRequestAttributes()
        .getAttribute(ATTRIBUTE_USER_ID, SCOPE_REQUEST);
  }
}
