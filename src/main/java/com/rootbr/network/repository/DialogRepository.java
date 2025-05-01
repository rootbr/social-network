package com.rootbr.network.repository;

import com.rootbr.network.model.DialogMessage;
import java.util.List;

public interface DialogRepository {
  void sendMessage(DialogMessage message);
  List<DialogMessage> getDialog(String userId1, String userId2);
}
