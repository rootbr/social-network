package com.rootbr.network.service;

import com.rootbr.network.model.User;
import com.rootbr.network.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId("test-id");
    testUser.setFirstName("John");
    testUser.setSecondName("Doe");
    testUser.setBirthdate(LocalDate.of(1990, 1, 1));
    testUser.setBiography("Test Bio");
    testUser.setCity("Test City");
  }

  @Test
  void registerUser() {
    when(userRepository.createUser(any(User.class), anyString())).thenReturn("test-id");

    String userId = userService.registerUser(testUser, "password");

    assertEquals("test-id", userId);
    verify(userRepository, times(1)).createUser(any(User.class), eq("password"));
  }

  @Test
  void getUserById() {
    when(userRepository.getUserById("test-id")).thenReturn(Optional.of(testUser));

    Optional<User> result = userService.getUserById("test-id");

    assertTrue(result.isPresent());
    assertEquals("John", result.get().getFirstName());
    verify(userRepository, times(1)).getUserById("test-id");
  }

  @Test
  void searchUsers() {
    List<User> users = Arrays.asList(testUser);
    when(userRepository.searchUsers("Jo", "Do")).thenReturn(users);

    List<User> result = userService.searchUsers("Jo", "Do");

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals("John", result.get(0).getFirstName());
    verify(userRepository, times(1)).searchUsers("Jo", "Do");
  }

  @Test
  void authenticateUser() {
    when(userRepository.authenticateUser("test-id", "password")).thenReturn(true);

    boolean result = userService.authenticateUser("test-id", "password");

    assertTrue(result);
    verify(userRepository, times(1)).authenticateUser("test-id", "password");
  }
}