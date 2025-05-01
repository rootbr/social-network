package com.rootbr.network.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {
  private final Map<String, String> tokenToUserIdMap = new ConcurrentHashMap<>();

  public String generateToken(String userId) {
    String token = UUID.randomUUID().toString();
    tokenToUserIdMap.put(token, userId);
    return token;
  }

  public String getUserIdFromToken(String token) {
    return tokenToUserIdMap.get(token);
  }

  public boolean isValidToken(String token) {
    return tokenToUserIdMap.containsKey(token);
  }
}
