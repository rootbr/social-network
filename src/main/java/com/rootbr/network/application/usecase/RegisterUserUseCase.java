package com.rootbr.network.application.usecase;

import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.domain.engine.CommandAuthor;
import java.io.IOException;
import java.time.LocalDate;

public interface RegisterUserUseCase {

  void registerUser(
      final CommandAuthor commandAuthor,
      final String id,
      final String firstName,
      final String lastName,
      final String city,
      final LocalDate birthdate,
      final String biography,
      final String encodedPassword,
      final JsonGenerator response
  ) throws IOException;
}
