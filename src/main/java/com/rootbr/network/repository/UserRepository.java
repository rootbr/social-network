package com.rootbr.network.repository;

import com.rootbr.network.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  String createUser(User user, String password);
  Optional<User> getUserById(String id);
  List<User> searchUsers(String firstName, String lastName);
  boolean authenticateUser(String id, String password);
  void addFriend(String userId, String friendId);
  void removeFriend(String userId, String friendId);
}
