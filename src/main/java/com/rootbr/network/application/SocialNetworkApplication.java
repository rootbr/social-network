package com.rootbr.network.application;

import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.port.db.UserPort;

public class SocialNetworkApplication {

  private final Principal.Factory principalFactory;
  private final AuthenticationService authenticationService;

  public SocialNetworkApplication(final AuthenticationService authenticationService, final UserPort userPort) {
    final AllUsers allUsers = new AllUsers(userPort);
    this.authenticationService = authenticationService;
    this.principalFactory = principalId -> new Principal(principalId, authenticationService, allUsers);
  }

  public Principal createPrincipal(final String userId, final String password) {
    authenticationService.createPrincipal(userId, password);
    return principalFactory.create(userId);
  }

  public Principal login(final String userId, final String password) {
    if (authenticationService.validatePassword(userId, password)) {
      return principalFactory.create(userId);
    } else {
      return null;
    }
  }

  public Principal authorize(final String jwt) {
    return principalFactory.create(authenticationService.extractPrincipalId(jwt));
  }
}