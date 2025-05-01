package com.rootbr.network.service;

import com.rootbr.network.model.User;
import com.rootbr.network.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public String registerUser(User user, String password) {
    user.setId(UUID.randomUUID().toString());
    return userRepository.createUser(user, password);
  }

  @Cacheable(value = "users", key = "#id")
  public Optional<User> getUserById(String id) {
    return userRepository.getUserById(id);
  }

  public List<User> searchUsers(String firstName, String lastName) {
    return userRepository.searchUsers(firstName, lastName);
  }

  public boolean authenticateUser(String id, String password) {
    return userRepository.authenticateUser(id, password);
  }

  public void addFriend(String userId, String friendId) {
    userRepository.addFriend(userId, friendId);
  }

  public void removeFriend(String userId, String friendId) {
    userRepository.removeFriend(userId, friendId);
  }
}
