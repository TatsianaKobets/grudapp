package org.example.grudapp.service;
import org.example.grudapp.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserServiceTest {
  private UserService userService;

  @Before
  public void setUp() {
    userService = new UserService();
  }

  @Test
  public void testRegisterUser() {
    String email = "test@example.com";
    String password = "password";
    String name = "Test User";

    userService.registerUser(email, password, name);

    User user = userService.getUserByEmail(email);
    assertNotNull(user);
    assertEquals(email, user.getEmail());
    assertEquals(password, user.getPassword());
    assertEquals(name, user.getName());
  }

  @Test
  public void testAuthenticateUser() {
    String email = "test@example.com";
    String password = "password";
    String name = "Test User";

    userService.registerUser(email, password, name);

    User authenticatedUser = userService.authenticateUser(email, password);
    assertNotNull(authenticatedUser);
    assertEquals(email, authenticatedUser.getEmail());
    assertEquals(password, authenticatedUser.getPassword());
    assertEquals(name, authenticatedUser.getName());
  }

  @Test
  public void testAuthenticateUserWrongPassword() {
    String email = "test@example.com";
    String password = "password";
    String name = "Test User";

    userService.registerUser(email, password, name);

    User authenticatedUser = userService.authenticateUser(email, "wrongpassword");
    assertNull(authenticatedUser);
  }

  @Test
  public void testGetUserByEmail() {
    String email = "test@example.com";
    String password = "password";
    String name = "Test User";

    userService.registerUser(email, password, name);

    User user = userService.getUserByEmail(email);
    assertNotNull(user);
    assertEquals(email, user.getEmail());
    assertEquals(password, user.getPassword());
    assertEquals(name, user.getName());
  }

  @Test
  public void testGetUserByEmailNotFound() {
    String email = "notfound@example.com";

    User user = userService.getUserByEmail(email);
    assertNull(user);
  }

  @Test
  public void testEditUserProfile() {
    String email = "test@example.com";
    String password = "password";
    String name = "Test User";

    userService.registerUser(email, password, name);

    User user = userService.getUserByEmail(email);
    user.setName("New Name");
    userService.editUserProfile(user);

    User updatedUser = userService.getUserByEmail(email);
    assertNotNull(updatedUser);
    assertEquals(email, updatedUser.getEmail());
    assertEquals(password, updatedUser.getPassword());
    assertEquals("New Name", updatedUser.getName());
  }

  @Test
  public void testDeleteUser() {
    String email = "test@example.com";
    String password = "password";
    String name = "Test User";

    userService.registerUser(email, password, name);

    userService.deleteUser(email);

    User user = userService.getUserByEmail(email);
    assertNull(user);
  }

  @Test
  public void testResetPassword() {
    String email = "test@example.com";
    String password = "password";
    String name = "Test User";

    userService.registerUser(email, password, name);

    userService.resetPassword(email);

    User user = userService.getUserByEmail(email);
    assertNotNull(user);
    assertNotEquals(password, user.getPassword());
  }
}
