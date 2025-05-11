package com.rootbr.network.application;

import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.UserVisitor;
import com.sun.net.httpserver.HttpPrincipal;
import java.io.IOException;
import java.time.LocalDate;

public class Principal extends HttpPrincipal {

  private final AuthenticationService authenticationService;
  private final AllUsers allUsers;

  public Principal(
      final String principalId,
      final AuthenticationService authenticationService,
      final AllUsers allUsers
  ) {
    super(principalId, "social-network");
    this.authenticationService = authenticationService;
    this.allUsers = allUsers;
  }

  public void registerUser(final String userId, final String firstName, final String lastName, final String city, final LocalDate birthdate, final String biography) {
    allUsers.registerNewUser(userId, firstName, lastName, city, birthdate, biography);
  }

  public String generateJwt() {
    return authenticationService.generateJwt(getUsername());
  }

  public void readUser(final String userId, final UserVisitor visitor) throws IOException {
    allUsers.getById(userId).write(visitor);
  }

  public interface Factory {
    Principal create(String principalId);
  }
}
