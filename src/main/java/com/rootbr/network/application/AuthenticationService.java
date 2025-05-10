package com.rootbr.network.application;

public interface AuthenticationService {

  String cryptPassword(String password);

  boolean validatePassword(String password, String hashedPassword);

  String generateToken(String id);

  String authentication(String jwt);
}
