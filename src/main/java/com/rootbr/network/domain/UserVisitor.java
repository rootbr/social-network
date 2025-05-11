package com.rootbr.network.domain;

import java.io.IOException;
import java.time.LocalDate;

public interface UserVisitor {

  void visitUser(
      final String id,
      final String firstName,
      final String lastName,
      final String city,
      final LocalDate birthdate,
      final String biography
  ) throws IOException;
}
