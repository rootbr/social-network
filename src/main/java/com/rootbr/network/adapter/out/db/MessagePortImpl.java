package com.rootbr.network.adapter.out.db;

import com.rootbr.network.application.port.MessagePort;
import com.rootbr.network.application.visitor.MessagesVisitor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class MessagePortImpl implements MessagePort {

  public static final String ROLE_SYSTEM = "system";
  public static final String ROLE_USER = "user";
  public static final String ROLE_ASSISTANT = "assistant";
  public static final String ROLE_TOOL = "tool";

  private static final String sqlCreateMessage = "INSERT INTO messages (chat_id, message_id, role, content, created_at) VALUES (?, ?, ?, ?, ?)";

  @Override
  public void createMessage(final Connection connection, final String chatId, final int messageId, final String role, final String content,
      final Instant createAt) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(sqlCreateMessage)) {
      ps.setString(1, chatId);
      ps.setInt(2, messageId);
      ps.setString(3, role);
      ps.setString(4, content);
      ps.setObject(5, Timestamp.from(createAt));
      ps.executeUpdate();
    }
  }

  private static final String sqlGetMessagesByChatId = "SELECT message_id, role, content, created_at FROM messages WHERE chat_id = ?";

  @Override
  public void getMessagesByChatId(final Connection connection, final String chatId, final MessagesVisitor messagesVisitor)
      throws Exception {
    try (final PreparedStatement ps = connection.prepareStatement(sqlGetMessagesByChatId)) {
      ps.setString(1, chatId);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          final int id = rs.getInt(1);
          final String role = rs.getString(2);
          final String content = rs.getString(3);
          final String createdAt = rs.getTimestamp(4).toString();
          messagesVisitor.visitMessage(id, role, content, createdAt, rs.isFirst(), rs.isLast());
        }
      }
    }
  }
}