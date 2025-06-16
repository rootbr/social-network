package com.rootbr.network.application.port;

import com.rootbr.network.application.visitor.MessagesVisitor;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;

public interface MessagePort {

  void getMessagesByChatId(Connection connection, String chatId, MessagesVisitor messagesVisitor)
      throws Exception;

  void createMessage(Connection connection, final String chatId, int messageId, final String role,
      String content, final Instant createAt) throws SQLException;
}