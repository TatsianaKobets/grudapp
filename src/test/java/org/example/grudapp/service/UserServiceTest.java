package org.example.grudapp.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import org.example.grudapp.PostgresContainerTest;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.TestcontainersExtension;

@ExtendWith(TestcontainersExtension.class)
class UserServiceTest extends PostgresContainerTest {

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService();
  }

  @Test
  void testGetSetConnection() throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
        "postgres", "password");
    userService.setConnection(connection);
    Connection result = userService.getConnection();

    assertEquals(connection, result);
  }

  @Test
  void testGetUsers() {
    String email1 = "test1@example.com";
    String password1 = "testpassword1";
    String username1 = "testuser1";
    userService.registerUser(email1, password1, username1);

    String email2 = "test2@example.com";
    String password2 = "testpassword2";
    String username2 = "testuser2";
    userService.registerUser(email2, password2, username2);
    Map<Integer, User> users = userService.getUsers();

    // We check that the list contains 4 users because we added 2 through the liquibase script
    assertEquals(4, users.size());
    User user1 = users.get(userService.getUserByEmail(email1).getId());
    assertEquals(email1, user1.getEmail());
    assertEquals(username1, user1.getName());

    User user2 = users.get(userService.getUserByEmail(email2).getId());
    assertEquals(email2, user2.getEmail());
    assertEquals(username2, user2.getName());
  }

  @Test
  void testGetUserByEmail() {
    String email = "testGetUserByEmail@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User user = userService.getUserByEmail(email);

    assertNotNull(user, "Пользователь должен быть найден.");
    assertEquals(email, user.getEmail(), "Электронная почта пользователя должна совпадать.");
    assertEquals(username, user.getName(), "Имя пользователя должно совпадать.");
  }

  @Test
  void testGetUserByEmail_notFound() {
    String email = "notfoundGetUserByEmail@example.com";
    User user = userService.getUserByEmail(email);
    assertNull(user, "Пользователь должен быть null.");
  }

  @Test
  void registerUser_success() {
    String email = "test@example.com";
    String password = "testpassword";
    String username = "testuser";

    User user = userService.registerUser(email, password, username);

    assertEquals(user.getId(), userService.getUserByEmail(email).getId());
    assertEquals(user.getEmail(), userService.getUserByEmail(email).getEmail());
    assertEquals(user.getName(), userService.getUserByEmail(email).getName());
    assertEquals(user.getRole(), userService.getUserByEmail(email).getRole());
  }

  @Test
  void registerUser_duplicateEmail() {
    String email = "test@example.com";
    String password = "testpassword";
    String username = "testuser";

    userService.registerUser(email, password, username);

    User user = userService.registerUser(email, password, username);

    assertNull(user);
  }

  @Test
  void testGetAuthenticatedUser_notAuthenticated() {
    User authenticatedUser = userService.getAuthenticatedUser();
    assertNull(authenticatedUser, "Аутентифицированный пользователь должен быть null.");
  }

  @Test
  void testGetAuthenticatedUser_authenticated() {
    String email = "testGetAuthenticatedUser@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);
    userService.authenticateUser(email, password);

    User authenticatedUser = userService.getAuthenticatedUser();
    assertNotNull(authenticatedUser, "Аутентифицированный пользователь должен быть найден.");
    assertEquals(email, authenticatedUser.getEmail(),
        "Электронная почта аутентифицированного пользователя должна совпадать.");
    assertEquals(username, authenticatedUser.getName(),
        "Имя аутентифицированного пользователя должно совпадать.");
  }

  @Test
  void testAuthenticateUser_success() {
    String email = "testAuthenticateUser@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User authenticatedUser = userService.authenticateUser(email, password);
    assertNotNull(authenticatedUser, "Аутентифицированный пользователь должен быть найден.");
    assertEquals(email, authenticatedUser.getEmail(),
        "Электронная почта аутентифицированного пользователя должна совпадать.");
    assertEquals(username, authenticatedUser.getName(),
        "Имя аутентифицированного пользователя должно совпадать.");
  }

  @Test
  void testAuthenticateUser_wrongPassword() {
    String email = "testAuthenticateUserWrongPassword@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User authenticatedUser = userService.authenticateUser(email, "wrongpassword");
    assertNull(authenticatedUser, "Аутентифицированный пользователь должен быть null");
  }

  @Test
  void testAuthenticateUser_notFound() {
    String email = "notfound@example.com";
    String password = "testpassword";

    User authenticatedUser = userService.authenticateUser(email, password);
    assertNull(authenticatedUser, "Аутентифицированный пользователь должен быть null");
  }

  @Test
  void testGetUserById_found() {
    String email = "testGetUserById@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User user = userService.getUserById(userService.getUserByEmail(email).getId());

    assertNotNull(user, "Пользователь должен быть найден.");
    assertEquals(email, user.getEmail(), "Электронная почта пользователя должна совпадать.");
    assertEquals(username, user.getName(), "Имя пользователя должно совпадать.");
  }

  @Test
  void testGetUserById_notFound() {
    User user = userService.getUserById(99999);
    assertNull(user, "Пользователь должен быть null.");
  }

  @Test
  void testGetUserById_invalidId() {
    User user = userService.getUserById(-1);
    assertNull(user, "Пользователь должен быть null.");
  }

  @Test
  void testEditUserProfile_success() {
    String email = "testEditUserProfile@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User user = userService.getUserByEmail(email);
    user.setEmail("newemailEditUserProfile@example.com");
    user.setPassword("newpassword");
    user.setName("newusername");

    userService.editUserProfile(user);

    User updatedUser = userService.getUserByEmail("newemailEditUserProfile@example.com");
    assertNotNull(updatedUser, "Пользователь должен быть найден.");
    assertEquals("newemailEditUserProfile@example.com", updatedUser.getEmail(),
        "Электронная почта пользователя должна совпадать.");
    assertEquals("newusername", updatedUser.getName(), "Имя пользователя должно совпадать.");
  }

  @Test
  void testEditUserProfile_notFound() {
    User user = new User();
    user.setId(99999);
    user.setEmail("newemailEditUserProfileNotFound@example.com");
    user.setPassword("newpassword");
    user.setName("newusername");

    userService.editUserProfile(user);

    User updatedUser = userService.getUserByEmail("newemailEditUserProfileNotFound@example.com");
    assertNull(updatedUser, "Пользователь должен быть null.");
  }

  @Test
  void testEditUserProfile_invalidId() {
    User user = new User();
    user.setId(-1);
    user.setEmail("newemailtEditUserProfile_invalidId@example.com");
    user.setPassword("newpassword");
    user.setName("newusername");

    userService.editUserProfile(user);

    User updatedUser = userService.getUserByEmail("newemailtEditUserProfile_invalidId@example.com");
    assertNull(updatedUser, "Пользователь должен быть null.");
  }

  @Test
  void testResetPassword_success() {
    String email = "testResetPassword@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User user = userService.getUserByEmail(email);
    String oldPassword = user.getPassword();

    userService.resetPassword(email);

    User updatedUser = userService.getUserByEmail(email);
    assertNotNull(updatedUser, "Пользователь должен быть найден.");
    assertNotEquals(oldPassword, updatedUser.getPassword(), "Пароль должен быть изменен.");
  }

  @Test
  void testResetPassword_notFound() {
    String email = "notfoundResetPassword@example.com";
    userService.resetPassword(email);
    User user = userService.getUserByEmail(email);
    assertNull(user, "Пользователь должен быть null.");
  }

  @Test
  void testGeneratePassword() {
    String password = userService.generatePassword();
    assertNotNull(password, "Пароль должен быть сгенерирован.");
    assertEquals(8, password.length(), "Длина пароля должна быть 8 символов.");
  }

  @Test
  void testGeneratePasswordDigits() {
    String password = userService.generatePassword();
    assertNotNull(password, "Пароль должен быть сгенерирован.");
    assertEquals(8, password.length(), "Длина пароля должна быть 8 символов.");

    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    for (char c : password.toCharArray()) {
      assertTrue(characters.indexOf(c) != -1, "Пароль содержит недопустимый символ.");
    }
  }

  @Test
  void testDeleteUser_success() {
    String email = "testDeleteUser@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    userService.deleteUser(email);

    User user = userService.getUserByEmail(email);
    assertNull(user, "Пользователь должен быть удален.");
  }

  @Test
  void testDeleteUser_notFound() {
    String email = "notfoundDeleteUser@example.com";
    userService.deleteUser(email);

    User user = userService.getUserByEmail(email);
    assertNull(user, "Пользователь должен быть null.");
  }

  @Test
  void testDeleteUser_emptyEmail() {
    String email = "";
    userService.deleteUser(email);

    assertTrue(true, "Метод должен корректно обработать пустую строку.");
  }

  @Test
  void testDeleteUser_nullEmail() {
    String email = null;
    userService.deleteUser(email);

    assertTrue(true, "Метод должен корректно обработать null.");
  }

  @Test
  void testAssignAdminRole_success() {
    String email = "testAssignAdminRole@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User user = userService.getUserByEmail(email);
    userService.assignAdminRole(user);

    Set<User> admins = userService.getAdmins();
    assertTrue(admins.contains(user), "Пользователь должен быть администратором.");
  }

  @Test
  void testAssignUserRole() {
    String email = "testAssignAdminRole@example.com";

    User user = userService.getUserByEmail(email);
    userService.assignUserRole(user);
    Set<User> admins = userService.getAdmins();
    assertFalse("Пользователь не должен быть администратором.", admins.contains(user));
  }

  @Test
  void testUpdateUserRole_success() {
    String email = "testUpdateUserRole@example.com";
    String password = "testpassword";
    String username = "testuser";
    userService.registerUser(email, password, username);

    User user = userService.getUserByEmail(email);
    user.setRole(Role.ADMIN);

    userService.updateUserRole(user);

    User updatedUser = userService.getUserByEmail(email);
    assertNotNull(updatedUser, "Пользователь должен быть найден.");
    assertEquals(Role.ADMIN, updatedUser.getRole(), "Роль пользователя должна быть обновлена.");
  }

  @Test
  void testUpdateUserRole_notFound() {
    User user = new User();
    user.setId(99999);
    user.setEmail("notfoundUpdateUserRole@example.com");
    user.setRole(Role.ADMIN);

    userService.updateUserRole(user);

    User updatedUser = userService.getUserByEmail("notfoundUpdateUserRole@example.com");
    assertNull(updatedUser, "Пользователь должен быть null.");
  }

  @Test
  void testUpdateUserRole_invalidId() {
    User user = new User();
    user.setId(-1);
    user.setEmail("invalidIdUpdateUserRole@example.com");
    user.setRole(Role.ADMIN);

    userService.updateUserRole(user);

    User updatedUser = userService.getUserByEmail("invalidIdUpdateUserRole@example.com");
    assertNull(updatedUser, "Пользователь должен быть null.");
  }
}

