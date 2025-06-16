package com.rootbr.network.adapter.out.db;

import com.rootbr.network.application.port.ParametersPort;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ParametersPortImpl implements ParametersPort {

  private static final String insertSql = "INSERT INTO variables (chat_id, key, value) VALUES (?, ?, ?)";

  @Override
  public void create(final Connection connection, final String chatId, final String parameterKey, final String parameterValue) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(insertSql)) {
      ps.setString(1, chatId);
      ps.setString(2, parameterKey);
      ps.setString(3, parameterValue);
      ps.executeUpdate();
    }
  }

  private static final String selectByChatIdSql = "SELECT key, value FROM variables WHERE chat_id = ?";

  @Override
  public Map<String, String> getParametersByChatId(final Connection connection, final String chatId) throws SQLException {
    final Map<String, String> parameters = new HashMap<>();
    try (final PreparedStatement ps = connection.prepareStatement(selectByChatIdSql)) {
      ps.setString(1, chatId);
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          parameters.put(rs.getString(1), rs.getString(2));
        }
      }
    }
    return parameters;
  }
}