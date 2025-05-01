package com.rootbr.network.service;

import com.rootbr.network.model.DialogMessage;
import com.rootbr.network.repository.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DialogService {
  private final DialogRepository dialogRepository;

  @Autowired
  public DialogService(DialogRepository dialogRepository) {
    this.dialogRepository = dialogRepository;
  }

  public void sendMessage(DialogMessage message) {
    dialogRepository.sendMessage(message);
  }

  public List<DialogMessage> getDialog(String userId1, String userId2) {
    return dialogRepository.getDialog(userId1, userId2);
  }
}
