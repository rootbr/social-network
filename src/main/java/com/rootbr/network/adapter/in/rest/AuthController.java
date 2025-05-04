package com.rootbr.network.adapter.in.rest;

import com.rootbr.network.adapter.in.rest.api.LoginRestApi;
import com.rootbr.network.application.usecase.LoginUseCase;
import com.rootbr.network.domain.port.rest.model.LoginPost200ResponseRestDto;
import com.rootbr.network.domain.port.rest.model.LoginPostRequestRestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements LoginRestApi {

  private final AuthService authService;
  private final LoginUseCase loginUseCase;

  @Autowired
  public AuthController(AuthService authService, LoginUseCase loginUseCase) {
    this.authService = authService;
    this.loginUseCase = loginUseCase;
  }

  @Override
  public ResponseEntity<LoginPost200ResponseRestDto> loginPost(final LoginPostRequestRestDto request) {
    if (loginUseCase.login(request)){
      return ResponseEntity.ok(
          LoginPost200ResponseRestDto.builder()
              .token(authService.generateToken(request.getId()))
              .build()
      );
    } else{
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
