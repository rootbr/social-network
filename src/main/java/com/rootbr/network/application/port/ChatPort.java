package com.rootbr.network.application.port;

import com.rootbr.network.application.visitor.ChatsVisitor;
import java.sql.Connection;
import java.time.Instant;

public interface ChatPort {

  void getChatsByUserId(Connection connection, String userId, ChatsVisitor chatsVisitor) throws Exception;

  String nextQuestionByChatId(Connection connection, String chatId) throws Exception;

  void createChat(Connection connection, String id, String userId, String title, String state, Instant updatedAt) throws Exception;

  void deleteChat(Connection connection, String chatId) throws Exception;

  void updateChat(Connection connection, String chatId, String state, Instant updatedAt, String lastMessage) throws Exception;
}