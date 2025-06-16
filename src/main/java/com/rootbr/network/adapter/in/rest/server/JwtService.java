package com.rootbr.network.adapter.in.rest.server;

import com.rootbr.network.adapter.in.rest.TokenService;
import com.rootbr.network.application.Principal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;
import java.util.function.BiFunction;
import javax.crypto.SecretKey;

public class JwtService implements TokenService {

  private final long tokenValidityInSeconds;
  private final SecretKey key;
  private final JwtParser jwtParser;

  public JwtService(final Properties properties) {
    this.tokenValidityInSeconds = Long.parseLong(properties.getProperty("expiration"));
    key = Keys.hmacShaKeyFor(properties.getProperty("secret").getBytes(StandardCharsets.UTF_8));
    jwtParser = Jwts.parser().verifyWith(key).build();
  }

  @Override
  public String generateToken(final String principalId, final String email) {
    final Date now = new Date();
    final Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);
    return Jwts.builder()
        .subject(principalId)
        .claim("email", email)
        .issuedAt(now)
        .expiration(validity)
        .signWith(key)
        .compact();
  }

  @Override
  public boolean validateToken(final String jwt) {
    try {
      final Claims payload = jwtParser.parseSignedClaims(jwt).getPayload();
      return payload != null;
    } catch (final Exception e) {
      return false;
    }
  }

  @Override
  public Principal createPrincipal(final String token, final BiFunction<String, String, Principal> factory) {
    try {
      final Claims payload = jwtParser.parseSignedClaims(token).getPayload();
      return factory.apply(payload.getSubject(), payload.get("email", String.class));
    } catch (final Exception e) {
      return null;
    }
  }
}