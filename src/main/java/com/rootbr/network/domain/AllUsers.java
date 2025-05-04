package com.rootbr.network.domain;

import com.rootbr.network.domain.port.db.UserPort;
import java.time.LocalDate;

public class AllUsers {

  private final UserPort userPort;

  public AllUsers(final UserPort userPort) {
    this.userPort = userPort;
  }

  public User registerNewUser(
      final String id,
      final String firstName,
      final String secondName,
      final String city,
      final LocalDate birthdate,
      final String biography,
      final String encodedPassword
  ) {
    final User newUser = new User(id, firstName, secondName, city, birthdate, biography, encodedPassword);
    newUser.write(userPort);
    return newUser;
  }

  public User getById(final String id) {
    return userPort.getUserById(id);
  }

  public Users search(final String firstName, final String lastName) {
    return userPort.searchUsers(firstName, lastName);
  }
}
