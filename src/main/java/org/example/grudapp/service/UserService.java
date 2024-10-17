package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.example.grudapp.dbconnect.DatabaseConnector;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;

/**
 * Provides services for managing users.
 */
public class UserService {
  private Connection connection;

  public UserService() {
    try {
      connection = DatabaseConnector.getConnection();
    } catch (SQLException e) {
      System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
    }
  }
  /**
   * List of all users.
   */
  private Map<Integer, User> users = new HashMap<>();
  /**
   * The currently authenticated user.
   */
  private User authenticatedUser;

  /**
   * Returns the list of all users.
   *
   * @return the map of users, where the key is the user ID and the value is the user object
   */
  public Map<Integer, User> getUsers() {
    return users;
  }

  /**
   * Returns the currently authenticated user.
   *
   * @return the authenticated user
   */
  public User getAuthenticatedUser() {
    return authenticatedUser;
  }

  /**
   * Registers a new user.
   *
   * @param email    the email address of the user
   * @param password the password of the user
   * @param name     the name of the user
   */
  public void registerUser(String email, String password, String name) {
    String query = "INSERT INTO users (email, password, name, role) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, email);
      statement.setString(2, password);
      statement.setString(3, name);
      if (getUserCount() == 0) {
        statement.setString(4, "ADMIN");
        System.out.println("Вам присвоена роль администратора");
      } else {
        statement.setString(4, "USER");
      }
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка регистрации пользователя: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Authenticates a user.
   *
   * @param email    the email address of the user
   * @param password the password of the user
   * @return the authenticated user, or null if authentication fails
   */
  public User authenticateUser(String email, String password) {
    String query = "SELECT * FROM users WHERE email = ? AND password = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, email);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        authenticatedUser = user;
        return user;
      }
    } catch (SQLException e) {
      System.out.println("Ошибка аутентификации пользователя: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
    return null;
  }

  /**
   * Returns a user by their ID.
   *
   * @param id the ID of the user
   * @return the user, or null if not found
   */
  public User getUserById(int id) {
    String query = "SELECT * FROM users WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        return user;
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения пользователя по ID: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
    return null;
  }


  /**
   * Returns a user by their email address.
   *
   * @param email the email address of the user
   * @return the user, or null if not found
   */
  public User getUserByEmail(String email) {
    String query = "SELECT * FROM users WHERE email = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setName(resultSet.getString("name"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        return user;
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения пользователя по email: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
    return null;
  }

  /**
   * Edits a user's profile.
   *
   * @param user the user to edit
   */
  public void editUserProfile(User user) {
    String query = "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail());
      statement.setString(3, user.getPassword());
      statement.setInt(4, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка редактирования профиля пользователя: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
  }


  /**
   * Resets a user's password.
   *
   * @param email the email address of the user
   */
  public void resetPassword(String email) {
    String newPassword = generatePassword();
    String query = "UPDATE users SET password = ? WHERE email = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, newPassword);
      statement.setString(2, email);
      statement.executeUpdate();
      System.out.println("Новый пароль: " + newPassword);
    } catch (SQLException e) {
      System.out.println("Ошибка сброса пароля: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Generates a random password.
   *
   * @return the generated password
   */
  public String generatePassword() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder password = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      password.append(characters.charAt((int) (Math.random() * characters.length())));
    }
    return password.toString();
  }

  /**
   * Deletes a user by their email address.
   *
   * @param email the email address of the user to delete
   */
  public void deleteUser(String email) {
    String query = "DELETE FROM users WHERE email = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, email);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка удаления пользователя: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
  }
  /**
   * Returns the number of users in the database.
   *
   * @return the number of users
   */
  public int getUserCount() {
    String query = "SELECT COUNT(*) FROM users";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения количества пользователей: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
    return 0;
  }
}
