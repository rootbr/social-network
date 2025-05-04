package com.rootbr.network.domain;

import com.rootbr.network.domain.port.db.UserPort;
import com.rootbr.network.domain.port.rest.model.UserRegisterPost200ResponseRestDto;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;

public class User {

  private final String id;
  private String firstName;
  private String secondName;
  private String city;
  private LocalDate birthdate;
  private String biography;
  private String encodedPassword;

  public User(
      final String id,
      final String firstName,
      final String secondName,
      final String city,
      final LocalDate birthdate,
      final String biography,
      final String encodedPassword
  ) {
    this.id = id;
    this.firstName = firstName;
    this.secondName = secondName;
    this.city = city;
    this.birthdate = birthdate;
    this.biography = biography;
    this.encodedPassword = encodedPassword;
  }

  public void write(final UserRegisterPost200ResponseRestDto.Builder response) {
    response.userId(id);
  }

  public void write(final UserRestDto.Builder response) {
    response.id(id);
    response.firstName(firstName);
    response.secondName(secondName);
    response.city(city);
    response.birthdate(birthdate);
    response.biography(biography);
  }

  public void write(final UserPort userPort) {
    userPort.createUser(id, firstName, secondName, city, birthdate, biography, encodedPassword);
  }

  public boolean login(final PasswordEncoder passwordEncoder, final String password) {
    return passwordEncoder.matches(password, encodedPassword);
  }
}
