package com.rootbr.network.application.port;

public interface PasswordEncoder {

  String encode(String rawPassword);

  boolean matches(String rawPassword, String encodedPassword);
}
