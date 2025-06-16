package com.rootbr.network.adapter.in.rest;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Hasher;
import at.favre.lib.crypto.bcrypt.BCrypt.Verifyer;
import com.rootbr.network.application.port.PasswordEncoder;
import java.util.Properties;

public class BCryptPasswordEncoder implements PasswordEncoder {

  private final int cost;
  private final Hasher hasher;
  private final Verifyer verifyer;

  public BCryptPasswordEncoder(final Properties properties) {
    this.cost = Integer.parseInt(properties.getProperty("cost"));
    this.hasher = BCrypt.withDefaults();
    this.verifyer = BCrypt.verifyer();
  }

  @Override
  public String encode(final String rawPassword) {
    return this.hasher.hashToString(cost, rawPassword.toCharArray());
  }

  @Override
  public boolean matches(final String rawPassword, final String encodedPassword) {
    return verifyer.verify(rawPassword.toCharArray(), encodedPassword).verified;
  }
}
