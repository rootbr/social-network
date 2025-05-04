package com.rootbr.network.adapter.in.rest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final String secretKey;
  private final long tokenValidityInSeconds;
  private Key key;

  // Store for revoked tokens
  private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

  public AuthService(@Value("${jwt.secret}") String secretKey,
                     @Value("${jwt.expiration}") long tokenValidityInSeconds) {
    this.secretKey = secretKey;
    this.tokenValidityInSeconds = tokenValidityInSeconds;
  }

  @PostConstruct
  public void init() {
    // Initialize the signing key
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String userId) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + tokenValidityInSeconds * 1000);

    return Jwts.builder()
        .setSubject(userId)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();
  }

  public String getUserIdFromToken(String token) {
    try {
      return extractClaim(token, Claims::getSubject);
    } catch (JwtException | IllegalArgumentException e) {
      return null;
    }
  }

  public boolean isValidToken(String token) {
    try {
      // Check if token is revoked
      if (revokedTokens.contains(token)) {
        return false;
      }

      // Check if token is expired
      Jwts.parser()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token);

      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  // For logout functionality
  public void revokeToken(String token) {
    revokedTokens.add(token);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}
