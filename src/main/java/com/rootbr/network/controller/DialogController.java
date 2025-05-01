package com.rootbr.network.controller;

import com.rootbr.network.dto.DialogMessageRequest;
import com.rootbr.network.model.DialogMessage;
import com.rootbr.network.service.AuthService;
import com.rootbr.network.service.DialogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dialog")
public class DialogController {
  private final DialogService dialogService;
  private final AuthService authService;

  @Autowired
  public DialogController(DialogService dialogService, AuthService authService) {
    this.dialogService = dialogService;
    this.authService = authService;
  }

  @PostMapping("/{user_id}/send")
  public ResponseEntity<Void> sendMessage(
      @PathVariable("user_id") String toUserId,
      @RequestBody DialogMessageRequest request,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String fromUserId = authService.getUserIdFromToken(token);

    if (fromUserId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    DialogMessage message = new DialogMessage();
    message.setFrom(fromUserId);
    message.setTo(toUserId);
    message.setText(request.getText());

    dialogService.sendMessage(message);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{user_id}/list")
  public ResponseEntity<List<DialogMessage>> getDialog(
      @PathVariable("user_id") String otherUserId,
      @RequestHeader("Authorization") String authHeader) {
    String token = authHeader.replace("Bearer ", "");
    String userId = authService.getUserIdFromToken(token);

    if (userId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    List<DialogMessage> dialog = dialogService.getDialog(userId, otherUserId);
    return ResponseEntity.ok(dialog);
  }
}
