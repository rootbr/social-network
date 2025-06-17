package com.rootbr.network.adapter.out.db;

import com.rootbr.network.application.port.UserPort;
import com.rootbr.network.application.visitor.UsersVisitor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserPortImpl implements UserPort {

  private static final String sqlCreate = "INSERT INTO users (id, first_name, last_name, birth_date, biography, city) VALUES (?, ?, ?, ?, ?, ?)";

  @Override
  public void create(final Connection connection, final String id, final String firstName, final String secondName,
                    final String birthdate, final String biography, final String city) throws SQLException {
    try (final PreparedStatement ps = connection.prepareStatement(sqlCreate)) {
      ps.setString(1, id);
      ps.setString(2, firstName);
      ps.setString(3, secondName);
      ps.setString(4, birthdate);
      ps.setString(5, biography);
      ps.setString(6, city);
      final int rowsAffected = ps.executeUpdate();
      if (rowsAffected == 0) {
        throw new RuntimeException("No rows were inserted");
      }
    }
  }

  private static final String sqlGetUserById = "SELECT id, first_name, last_name, birth_date, biography, city FROM users WHERE id = ?";

  @Override
  public void readUserData(final Connection connection, final String id, final UsersVisitor usersVisitor) throws Exception {
    try (final PreparedStatement ps = connection.prepareStatement(sqlGetUserById)) {
      ps.setString(1, id);
      try (final ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          usersVisitor.visitUser(rs.getString(1), rs.getString(2), rs.getString(3),
                               rs.getString(4), rs.getString(5), rs.getString(6));
        }
      }
    }
  }

  @Override
  public void getUserById(final Connection connection, final String userId, final UsersVisitor visitor) throws Exception {
    readUserData(connection, userId, visitor);
  }

  private static final String sqlSearchUsers = "SELECT id, first_name, last_name, birth_date, biography, city FROM users WHERE first_name LIKE ? AND last_name LIKE ?";

  @Override
  public void searchUsers(final Connection connection, final String firstName, final String lastName, final UsersVisitor visitor) throws Exception {
    try (final PreparedStatement ps = connection.prepareStatement(sqlSearchUsers)) {
      ps.setString(1, firstName + "%");
      ps.setString(2, lastName + "%");
      try (final ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          visitor.visitUser(rs.getString(1), rs.getString(2), rs.getString(3),
                          rs.getString(4), rs.getString(5), rs.getString(6));
        }
      }
    }
  }
}