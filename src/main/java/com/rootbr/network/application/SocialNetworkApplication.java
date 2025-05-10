package com.rootbr.network.application;

import com.fasterxml.jackson.core.JsonGenerator;
import com.rootbr.network.application.usecase.GetUserByIdUseCase;
import com.rootbr.network.application.usecase.RegisterUserUseCase;
import com.rootbr.network.application.usecase.SearchUsersByNameUseCase;
import com.rootbr.network.application.usecase.impl.GetUserByIdUseCaseImpl;
import com.rootbr.network.application.usecase.impl.RegisterUserUseCaseImpl;
import com.rootbr.network.application.usecase.impl.SearchUsersByNameUseCaseImpl;
import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.engine.Invoker;
import com.rootbr.network.domain.port.db.UserPort;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public class SocialNetworkApplication {

  public static final CommandAuthor ANONYMOUS = new CommandAuthor("anonymous");

  private final AllUsers allUsers;
  private final RegisterUserUseCase registerUserUseCase;
  private final GetUserByIdUseCase getUserByIdUseCase;
  private final SearchUsersByNameUseCase searchUsersByNameUseCase;

  public SocialNetworkApplication(final UserPort userPort) {
    this.allUsers = new AllUsers(userPort);
    final Invoker invoker = new Invoker();
    this.registerUserUseCase = new RegisterUserUseCaseImpl(allUsers, invoker);
    this.getUserByIdUseCase = new GetUserByIdUseCaseImpl(allUsers, invoker);
    this.searchUsersByNameUseCase = new SearchUsersByNameUseCaseImpl(allUsers, invoker);
  }

  public void getUserById(final CommandAuthor commandAuthor, final String id,
      final UserRestDto.Builder response)
      throws IOException {
    getUserByIdUseCase.getUserById(commandAuthor, id, response);
  }

  public void registerUser(
      final CommandAuthor commandAuthor,
      final String id,
      final String firstName,
      final String lastName,
      final String city,
      final LocalDate birthdate,
      final String biography,
      final String encodedPassword,
      final JsonGenerator response
  ) throws IOException {
    registerUserUseCase.registerUser(commandAuthor, id, firstName, lastName, city, birthdate,
        biography, encodedPassword, response);
  }

  public void searchUsers(final CommandAuthor commandAuthor, final String firstName,
      final String lastName, final List<UserRestDto> response)
      throws IOException {
    searchUsersByNameUseCase.searchUsers(commandAuthor, firstName, lastName, response);
  }

  public Principal login(final String userId, final String password) {
    return null;
  }
}