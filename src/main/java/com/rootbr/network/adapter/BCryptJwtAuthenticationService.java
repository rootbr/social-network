package com.rootbr.network.adapter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import com.rootbr.network.application.AuthenticationService;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class BCryptJwtAuthenticationService implements AuthenticationService {

  private final long tokenValidityInSeconds;
  private final SecretKey key;
  private final Hasher hasher;
  private final Verifyer verifyer;
  private final JwtParser jwtParser;

  public BCryptJwtAuthenticationService(final String secretKey, final long tokenValidityInSeconds) {
    this.tokenValidityInSeconds = tokenValidityInSeconds;
    key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    jwtParser = Jwts.parser().verifyWith(key).build();
    hasher = BCrypt.withDefaults();
    verifyer = BCrypt.verifyer();
  }

  @Override
  public String cryptPassword(final String password) {
    return hasher.hashToString(12, password.toCharArray());
  }

  @Override
  public boolean validatePassword(final String password, final String hashedPassword) {
    return verifyer.verify(password.toCharArray(), hashedPassword).verified;
  }

  @Override
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

  @Override
  public String authentication(final String jwt) {
    try {
      return jwtParser.parseSignedClaims(jwt).getPayload().getSubject();
    } catch (final Exception e) {
      return null;
    }
  }
}
