package com.rootbr.network.domain;

import com.rootbr.network.domain.port.db.UserPort;
import java.time.LocalDate;

public class AllUsers {

  private final UserPort userPort;

  public AllUsers(final UserPort userPort) {
    this.userPort = userPort;
  }

  public void registerNewUser(
      final String id,
      final String firstName,
      final String secondName,
      final String city,
      final LocalDate birthdate,
      final String biography
  ) {
    final User newUser = new User(id, firstName, secondName, city, birthdate, biography);
    newUser.write(userPort);
  }

  public User getById(final String id) {
    return userPort.getUserById(id);
  }

  public Users search(final String firstName, final String lastName) {
    return userPort.searchUsers(firstName, lastName);
  }
}
