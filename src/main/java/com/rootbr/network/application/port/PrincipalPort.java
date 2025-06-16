package com.rootbr.network.application.port;

import com.rootbr.network.application.visitor.PrincipalVisitor;
import java.sql.Connection;
import java.sql.SQLException;

public interface PrincipalPort {

  void create(Connection connection, String principalId, String login, String encodedPassword) throws SQLException;

  void findPrincipalByLogin(Connection connection, String login, final PrincipalVisitor visitor) throws SQLException;

  void readEncodedPasswordByPrincipalId(Connection connection, String principalId, PrincipalVisitor visitor) throws SQLException;

  void changePassword(Connection connection, String principalId, String encodedPassword) throws SQLException;
}
