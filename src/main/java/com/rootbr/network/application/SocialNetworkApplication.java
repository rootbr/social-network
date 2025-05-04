package com.rootbr.network.application;

import com.rootbr.network.application.usecase.GetUserByIdUseCase;
import com.rootbr.network.application.usecase.LoginUseCase;
import com.rootbr.network.application.usecase.RegisterUserUseCase;
import com.rootbr.network.application.usecase.SearchUsersByNameUseCase;
import com.rootbr.network.application.usecase.impl.GetUserByIdUseCaseImpl;
import com.rootbr.network.application.usecase.impl.LoginUseCaseImpl;
import com.rootbr.network.application.usecase.impl.RegisterUserUseCaseImpl;
import com.rootbr.network.application.usecase.impl.SearchUsersByNameUseCaseImpl;
import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.engine.Invoker;
import com.rootbr.network.domain.port.db.UserPort;
import com.rootbr.network.domain.port.rest.model.LoginPostRequestRestDto;
import com.rootbr.network.domain.port.rest.model.UserRegisterPost200ResponseRestDto.Builder;
import com.rootbr.network.domain.port.rest.model.UserRegisterPostRequestRestDto;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SocialNetworkApplication implements
    RegisterUserUseCase, GetUserByIdUseCase, SearchUsersByNameUseCase, LoginUseCase {

  public static final CommandAuthor ANONYMOUS = new CommandAuthor("anonymous");

  private final AllUsers allUsers;
  private final RegisterUserUseCase registerUserUseCase;
  private final GetUserByIdUseCase getUserByIdUseCase;
  private final SearchUsersByNameUseCase searchUsersByNameUseCase;
  private final LoginUseCase loginUseCase;

  public SocialNetworkApplication(final UserPort userPort, final PasswordEncoder passwordEncoder) {
    this.allUsers = new AllUsers(userPort);
    final Invoker invoker = new Invoker();
    this.registerUserUseCase = new RegisterUserUseCaseImpl(allUsers, passwordEncoder, invoker);
    this.getUserByIdUseCase = new GetUserByIdUseCaseImpl(allUsers, invoker);
    this.searchUsersByNameUseCase = new SearchUsersByNameUseCaseImpl(allUsers, invoker);
    this.loginUseCase = new LoginUseCaseImpl(allUsers, passwordEncoder, invoker);
  }


  @Override
  public void getUserById(final CommandAuthor commandAuthor, final String id, final UserRestDto.Builder response) {
    getUserByIdUseCase.getUserById(commandAuthor, id, response);
  }

  @Override
  public void registerUser(final CommandAuthor commandAuthor, final UserRegisterPostRequestRestDto request, final Builder response) {
    registerUserUseCase.registerUser(commandAuthor, request, response);
  }

  @Override
  public void searchUsers(final CommandAuthor commandAuthor, final String firstName, final String lastName, final List<UserRestDto> response) {
    searchUsersByNameUseCase.searchUsers(commandAuthor, firstName, lastName, response);
  }

  @Override
  public boolean login(final LoginPostRequestRestDto request) {
    return loginUseCase.login(request);
  }
}