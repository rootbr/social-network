package com.rootbr.network.application.usecase;

import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.port.rest.model.UserRegisterPost200ResponseRestDto.Builder;
import com.rootbr.network.domain.port.rest.model.UserRegisterPostRequestRestDto;
import java.time.LocalDate;

public interface RegisterUserUseCase {

  void registerUser(
      final CommandAuthor commandAuthor,
      final String id,
      final String firstName,
      final String secondName,
      final String city,
      final LocalDate birthdate,
      final String biography,
      final String encodedPassword,
      final Builder response
  );
}
