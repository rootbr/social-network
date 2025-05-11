package com.rootbr.network.application.port;

public interface PrincipalPort {

  void save(String principalId, String encodedPassword);

  String getEncodedPassword(final String principalId);
}
