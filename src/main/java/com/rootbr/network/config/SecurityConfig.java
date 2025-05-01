package com.rootbr.network.config;

import com.rootbr.network.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Configuration
public class SecurityConfig {

  private static final List<String> PUBLIC_ENDPOINTS = List.of(
      "/login",
      "/user/register",
      "/user/get/",
      "/user/search",
      "/swagger-ui.html",
      "/swagger-ui/",
      "/v3/api-docs",
      "/swagger-resources",
      "/webjars/",
      "/swagger-ui/index.html",
      "/v3/api-docs/swagger-config"
  );

  @Autowired
  private AuthService authService;

  @Bean
  public OncePerRequestFilter authenticationFilter() {
    return new OncePerRequestFilter() {
      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
          FilterChain filterChain)
          throws ServletException, IOException {

        String path = request.getRequestURI();

        // Allow public endpoints
        boolean isPublicEndpoint = PUBLIC_ENDPOINTS.stream()
            .anyMatch(endpoint -> path.startsWith(endpoint) ||
                (endpoint.endsWith("/") && path.startsWith(
                    endpoint.substring(0, endpoint.length() - 1))));

        if (isPublicEndpoint) {
          filterChain.doFilter(request, response);
          return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return;
        }

        String token = authHeader.replace("Bearer ", "");

        if (!authService.isValidToken(token)) {
          response.setStatus(HttpStatus.UNAUTHORIZED.value());
          return;
        }

        filterChain.doFilter(request, response);
      }
    };
  }
}