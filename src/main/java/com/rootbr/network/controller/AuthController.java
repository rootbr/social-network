package com.rootbr.network.controller;

import com.rootbr.network.dto.LoginRequest;
import com.rootbr.network.dto.LoginResponse;
import com.rootbr.network.service.AuthService;
import com.rootbr.network.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
  private final UserService userService;
  private final AuthService authService;

  @Autowired
  public AuthController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    if (userService.authenticateUser(request.getId(), request.getPassword())) {
      String token = authService.generateToken(request.getId());
      LoginResponse response = new LoginResponse();
      response.setToken(token);
      return ResponseEntity.ok(response);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
