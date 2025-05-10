package com.rootbr.network.adapter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import com.rootbr.network.application.AuthenticationService;
import com.rootbr.network.application.PrincipalPort;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class BCryptJwtAuthenticationService implements AuthenticationService {

  private final PrincipalPort principalPort;
  private final long tokenValidityInSeconds;
  private final SecretKey key;
  private final Hasher hasher;
  private final Verifyer verifyer;
  private final JwtParser jwtParser;

  public BCryptJwtAuthenticationService(final PrincipalPort principalPort, final String secretKey, final long tokenValidityInSeconds) {
    this.principalPort = principalPort;
    this.tokenValidityInSeconds = tokenValidityInSeconds;
    key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    jwtParser = Jwts.parser().verifyWith(key).build();
    hasher = BCrypt.withDefaults();
    verifyer = BCrypt.verifyer();
  }

  @Override
  public void createPrincipal(final String principalId, final String password) {
    principalPort.save(principalId, hasher.hashToString(12, password.toCharArray()));
  }

  @Override
  public boolean validatePassword(final String principalId, final String password) {
    final String encodedPassword = principalPort.getEncodedPassword(principalId);
    return verifyer.verify(password.toCharArray(), encodedPassword).verified;
  }

  @Override
  public String generateJwt(final String principalId) {
    final Date now = new Date();
    final Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);
    return Jwts.builder()
        .subject(principalId)
        .issuedAt(now)
        .expiration(validity)
        .signWith(key)
        .compact();
  }

  @Override
  public String extractPrincipalId(final String jwt) {
    try {
      return jwtParser.parseSignedClaims(jwt).getPayload().getSubject();
    } catch (final Exception e) {
      return null;
    }
  }
}
