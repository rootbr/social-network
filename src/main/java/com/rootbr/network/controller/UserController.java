package com.rootbr.network.controller;

import com.rootbr.network.dto.UserRegisterRequest;
import com.rootbr.network.dto.UserRegisterResponse;
import com.rootbr.network.model.User;
import com.rootbr.network.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserRegisterRequest request) {
    User user = new User();
    user.setFirstName(request.getFirstName());
    user.setSecondName(request.getSecondName());
    user.setBirthdate(request.getBirthdate());
    user.setBiography(request.getBiography());
    user.setCity(request.getCity());

    String userId = userService.registerUser(user, request.getPassword());

    UserRegisterResponse response = new UserRegisterResponse();
    response.setUserId(userId);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<User> getUserById(@PathVariable String id) {
    Optional<User> user = userService.getUserById(id);
    return user.map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/search")
  public ResponseEntity<List<User>> searchUsers(
      @RequestParam("first_name") String firstName,
      @RequestParam("last_name") String lastName) {
    List<User> users = userService.searchUsers(firstName, lastName);
    return ResponseEntity.ok(users);
  }
}
