package com.rootbr.network.domain;

import java.io.IOException;
import java.time.LocalDate;

public class User {

  private final String id;
  private String firstName;
  private String lastName;
  private String city;
  private LocalDate birthdate;
  private String biography;

  public User(
      final String id,
      final String firstName,
      final String lastName,
      final String city,
      final LocalDate birthdate,
      final String biography
  ) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.city = city;
    this.birthdate = birthdate;
    this.biography = biography;
  }

  public void write(final UserVisitor visitor) throws IOException {
    visitor.visitUser(id, firstName, lastName, city, birthdate, biography);
  }
}
