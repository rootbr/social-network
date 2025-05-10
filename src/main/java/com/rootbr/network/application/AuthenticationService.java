package com.rootbr.network.application;

public interface AuthenticationService {

  boolean validatePassword(String password, String hashedPassword);

  String generateToken(String id);

  String authentication(String jwt);

  void createPrincipal(String userId, String password);
}
