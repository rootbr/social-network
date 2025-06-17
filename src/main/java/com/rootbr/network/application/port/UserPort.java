package com.rootbr.network.application.port;

import com.rootbr.network.application.visitor.UsersVisitor;
import java.sql.Connection;

public interface UserPort {

  void create(Connection connection, String id, String firstName, String secondName,
              String birthdate, String biography, String city) throws Exception;

  void readUserData(Connection connection, String id, UsersVisitor usersVisitor) throws Exception;

  void getUserById(Connection connection, String userId, UsersVisitor visitor) throws Exception;

  void searchUsers(Connection connection, String firstName, String lastName, UsersVisitor visitor) throws Exception;
}
