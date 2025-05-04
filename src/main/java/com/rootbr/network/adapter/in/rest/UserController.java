package com.rootbr.network.adapter.in.rest;

import static com.rootbr.network.adapter.RestApplicationConfig.getUserId;
import static com.rootbr.network.application.SocialNetworkApplication.ANONYMOUS;

import com.rootbr.network.adapter.in.rest.api.UserRestApi;
import com.rootbr.network.application.usecase.GetUserByIdUseCase;
import com.rootbr.network.application.usecase.RegisterUserUseCase;
import com.rootbr.network.application.usecase.SearchUsersByNameUseCase;
import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.port.rest.model.UserRegisterPost200ResponseRestDto;
import com.rootbr.network.domain.port.rest.model.UserRegisterPostRequestRestDto;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserRestApi {

  private final RegisterUserUseCase registerUserUseCase;
  private final GetUserByIdUseCase getUserByIdUseCase;
  private final SearchUsersByNameUseCase searchUsersByNameUseCase;

  @Autowired
  public UserController(
      final RegisterUserUseCase registerUserUseCase,
      final GetUserByIdUseCase getUserByIdUseCase,
      final SearchUsersByNameUseCase searchUsersByNameUseCase
  ) {
    this.registerUserUseCase = registerUserUseCase;
    this.getUserByIdUseCase = getUserByIdUseCase;
    this.searchUsersByNameUseCase = searchUsersByNameUseCase;
  }


  @Override
  public ResponseEntity<UserRestDto> userGetIdGet(final String id) {
    final UserRestDto.Builder responseBuilder = UserRestDto.builder();
    getUserByIdUseCase.getUserById(new CommandAuthor(getUserId()), id, responseBuilder);
    return ResponseEntity.ok(responseBuilder.build());
  }

  @Override
  public ResponseEntity<UserRegisterPost200ResponseRestDto> userRegisterPost(
      final UserRegisterPostRequestRestDto request) {
    final UserRegisterPost200ResponseRestDto.Builder responseBuilder = UserRegisterPost200ResponseRestDto.builder();
    registerUserUseCase.registerUser(ANONYMOUS, request, responseBuilder);
    return ResponseEntity.ok(responseBuilder.build());
  }

  @Override
  public ResponseEntity<List<UserRestDto>> userSearchGet(final String firstName,
      final String lastName) {
    List<UserRestDto> response = new ArrayList<>();
    searchUsersByNameUseCase.searchUsers(new CommandAuthor(getUserId()), firstName, lastName, response);
    return ResponseEntity.ok(response);
  }


}
