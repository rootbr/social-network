package com.rootbr.network.domain.port.db;

import com.rootbr.network.domain.User;
import com.rootbr.network.domain.Users;
import java.time.LocalDate;

public interface UserPort {

  User getUserById(String id);

  Users searchUsers(String firstName, String lastName);

  void createUser(
      final String id,
      final String firstName,
      final String secondName,
      final String city,
      final LocalDate birthdate,
      final String biography,
      final String encodedPassword
  );
}
