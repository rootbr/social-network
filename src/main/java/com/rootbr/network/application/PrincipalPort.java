package com.rootbr.network.application;

public interface PrincipalPort {

  void save(String principalId, String encodedPassword);

  String getEncodedPassword(final String principalId);
}
