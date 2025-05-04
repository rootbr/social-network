package com.rootbr.network.application.usecase;

import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.port.rest.model.UserRegisterPost200ResponseRestDto.Builder;
import com.rootbr.network.domain.port.rest.model.UserRegisterPostRequestRestDto;

public interface RegisterUserUseCase {

  void registerUser(final CommandAuthor commandAuthor, UserRegisterPostRequestRestDto request, Builder response);
}
