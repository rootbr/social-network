package com.rootbr.network.application;

public interface AuthenticationService {

  void createPrincipal(final String principalId, final String password);

  boolean validatePassword(final String principalId, final String password);

  String generateJwt(final String principalId);

  String extractPrincipalId(final String jwt);
}
