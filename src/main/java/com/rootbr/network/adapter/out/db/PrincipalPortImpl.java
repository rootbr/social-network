package com.rootbr.network.adapter.out.db;

import com.rootbr.network.application.port.PrincipalPort;
import com.rootbr.network.application.visitor.PrincipalVisitor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrincipalPortImpl implements PrincipalPort {

  private static final String sqlCreate = "INSERT INTO principals (id, login, encoded_password) VALUES (?, ?, ?)";

  @Override
  public void create(final Connection connection, final String principalId, final String login, final String encodedPassword) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(sqlCreate)) {
      ps.setString(1, principalId);
      ps.setString(2, login);
      ps.setString(3, encodedPassword);
      ps.executeUpdate();
    }
  }

  private static final String sqlFindPrincipalByLogin = "SELECT id, encoded_password FROM principals WHERE login = ?";

  @Override
  public void findPrincipalByLogin(final Connection connection, final String login, final PrincipalVisitor visitor) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(sqlFindPrincipalByLogin)) {
      ps.setString(1, login);
      try (final ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          visitor.visitId(rs.getString(1));
          visitor.visitEncodedPassword(rs.getString(2));
        }
      }
    }
  }

  private static final String sqlFindPrincipalById = "SELECT encoded_password FROM principals WHERE id = ?";

  @Override
  public void readEncodedPasswordByPrincipalId(final Connection connection, final String principalId, final PrincipalVisitor visitor) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(sqlFindPrincipalById)) {
      ps.setString(1, principalId);
      try (final ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          visitor.visitEncodedPassword(rs.getString(1));
        }
      }
    }
  }

  private static final String sqlChangePassword = "UPDATE principals SET encoded_password = ? WHERE id = ?";

  @Override
  public void changePassword(final Connection connection, final String principalId, final String encodedPassword) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(sqlChangePassword)) {
      ps.setString(1, encodedPassword);
      ps.setString(2, principalId);
      ps.executeUpdate();
    }
  }
}
