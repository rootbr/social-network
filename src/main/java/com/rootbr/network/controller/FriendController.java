package com.rootbr.network.controller;

import com.rootbr.network.service.AuthService;
import com.rootbr.network.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
public class FriendController {
  private final UserService userService;
  private final AuthService authService;

  @Autowired
  public FriendController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
  }

  @PutMapping("/set/{user_id}")
  public ResponseEntity<Void> addFriend(
      @PathVariable("user_id") String friendId,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String userId = authService.getUserIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    userService.addFriend(userId, friendId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/delete/{user_id}")
  public ResponseEntity<Void> removeFriend(
      @PathVariable("user_id") String friendId,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String userId = authService.getUserIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    userService.removeFriend(userId, friendId);
    return ResponseEntity.ok().build();
  }
}
