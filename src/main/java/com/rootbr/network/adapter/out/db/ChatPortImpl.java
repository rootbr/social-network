package com.rootbr.network.adapter.out.db;

import com.rootbr.network.application.port.ChatPort;
import com.rootbr.network.application.visitor.ChatsVisitor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class ChatPortImpl implements ChatPort {

  private static final String insertSql = "INSERT INTO chats (id, user_id, title, state, updated_at) VALUES (?, ?, ?, ?, ?)";

  @Override
  public void createChat(final Connection connection, final String id, final String userId, final String title,
      final String state, final Instant updatedAt) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(insertSql)) {
      ps.setString(1, id);
      ps.setString(2, userId);
      ps.setString(3, title);
      ps.setString(4, state);
      ps.setTimestamp(5, Timestamp.from(updatedAt));
      ps.executeUpdate();
    }
  }

  private static final String deleteChatSql = "DELETE FROM chats WHERE id = ?";

  @Override
  public void deleteChat(final Connection connection, final String chatId) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(deleteChatSql)) {
      ps.setString(1, chatId);
      final int rowsAffected = ps.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("Chat not found or access denied");
      }
    }
  }

  private static final String sqlGetStateChatByChatId = "SELECT state FROM chats WHERE id = ?";

  @Override
  public String nextQuestionByChatId(final Connection connection, final String chatId) throws Exception {
    String state = null;
    try (final PreparedStatement ps = connection.prepareStatement(sqlGetStateChatByChatId)) {
      ps.setString(1, chatId);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          state = rs.getString(1);
        }
      }
    }
    return state;
  }

  private static final String sqlGetChatsByUserId = "SELECT id, title, last_message, updated_at FROM chats WHERE user_id = ? ORDER BY updated_at DESC";

  @Override
  public void getChatsByUserId(final Connection connection, final String userId, final ChatsVisitor chatsVisitor) throws Exception {
    try (final PreparedStatement ps = connection.prepareStatement(sqlGetChatsByUserId)) {
      ps.setString(1, userId);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          chatsVisitor.visitChat(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4).toLocalDateTime().toString());
        }
      }
    }
  }

  private static final String updateChatSql = "UPDATE chats SET state = ?, updated_at = ?, last_message = ? WHERE id = ?";

  @Override
  public void updateChat(final Connection connection, final String chatId, final String state, final Instant updatedAt, final String lastMessage) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(updateChatSql)) {
      ps.setString(1, state);
      ps.setTimestamp(2, Timestamp.from(updatedAt));
      ps.setString(3, lastMessage != null && lastMessage.length() > 255 ? lastMessage.substring(0, 255) : lastMessage);
      ps.setString(4, chatId);
      final int rowsAffected = ps.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("Chat not found");
      }
    }
  }
}