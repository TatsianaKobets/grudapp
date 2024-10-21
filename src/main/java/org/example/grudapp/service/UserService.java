package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;

/**
 * Provides services for managing users.
 */
public class UserService {

  private Map<Integer, User> users = new HashMap<>();
  /**
   * The currently authenticated user.
   */
  private User authenticatedUser;
  private Connection connection;


  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  /**
   * Returns a database connection. If a connection has been previously injected,
   * it is returned. Otherwise, a new connection is created using the provided URL,
   * username, and password.
   *
   * @return a database connection
   * @throws SQLException if a database access error occurs
   */
  public Connection getConnection() throws SQLException {
    // Return the injected connection if available; otherwise, create a new one
    if (connection != null) {
      return connection;
    } else {
      return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
  }

  /**
   * Получить всех пользователей.
   *
   * @return Мапа пользователей, где ключ - ID пользователя, значение - объект пользователя
   */
  public Map<Integer, User> getUsers() {
    Map<Integer, User> users = new HashMap<>();
    String sql = "SELECT * FROM postgres_schema.users";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {

      while (rs.next()) {
        User user = new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("name")
        );
        user.setRole(Role.valueOf(rs.getString("role"))); // Преобразование значения роли
        users.put(user.getId(), user);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при получении пользователей: " + e.getMessage());
    }

    return users;
  }


  // Метод для возвращения текущего аутентифицированного пользователя
  public User getAuthenticatedUser(String email) {
    return getUserByEmail(
        email); // Если у вас есть метод getUserByEmail, можно использовать его здесь
  }

  // Метод поиска пользователя по email
  public User getUserByEmail(String email) {
    String sql = "SELECT * FROM postgres_schema.users WHERE email = ?";
    User user = null;

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, email);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        user = new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("name")
        );
        user.setRole(Role.valueOf(rs.getString("role")));
      }

    } catch (SQLException e) {
      System.out.println("Ошибка при получении пользователя по email: " + e.getMessage());
    }

    return user;
  }

  /**
   * Регистрация нового пользователя.
   *
   * @param email    email пользователя
   * @param password пароль пользователя
   * @param name     имя пользователя
   */
  public User registerUser(String email, String password, String name) {
    if (getUserByEmail(email) != null) {
      System.out.println("Пользователь с таким email уже существует");
      return null;
    }

    String sql = "INSERT INTO postgres_schema.users (email, password, name, role) VALUES (?, ?, ?, ?) RETURNING id";
    Role role = getUsers().isEmpty() ? Role.ADMIN : Role.USER;

    try (Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, email);
      pstmt.setString(2, password);
      pstmt.setString(3, name);
      pstmt.setString(4, role.toString());

      pstmt.executeUpdate();
      ResultSet rs = pstmt.getGeneratedKeys();
      if (rs.next()) {
        User user = new User(
            rs.getInt(1),
            email,
            password,
            name
        );
        user.setRole(role);
        return user;
      }

    } catch (SQLException e) {
      System.out.println("Ошибка при регистрации пользователя: " + e.getMessage());
    }
    return null;
  }

  public User getAuthenticatedUser() {
    return authenticatedUser; // Возврат аутентифицированного пользователя
  }

  public User authenticateUser(String email, String password) {
    String sql = "SELECT * FROM postgres_schema.users WHERE email = ? AND password = ?";
    User authenticatedUser = null;

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, email);
      pstmt.setString(2, password);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        authenticatedUser = new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("name")
        );
        authenticatedUser.setRole(Role.valueOf(rs.getString("role")));
        this.authenticatedUser = authenticatedUser; // Сохраняем аутентифицированного пользователя
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при аутентификации пользователя: " + e.getMessage());
    }
    return authenticatedUser;
  }

  /**
   * Получить пользователя по ID.
   *
   * @param id ID пользователя
   * @return пользователь или null
   */
  public User getUserById(int id) {
    String sql = "SELECT * FROM postgres_schema.users WHERE id = ?";
    User user = null;

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, id);
      ResultSet rs = pstmt.executeQuery();

      if (rs.next()) {
        user = new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("name")
        );
        user.setRole(Role.valueOf(rs.getString("role")));
      }

    } catch (SQLException e) {
      System.out.println("Ошибка при получении пользователя: " + e.getMessage());
    }

    return user;
  }


  // Обновление профиля пользователя
  public void editUserProfile(User user) {
    String sql = "UPDATE postgres_schema.users SET email = ?, password = ?, name = ? WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, user.getEmail());
      pstmt.setString(2, user.getPassword());
      pstmt.setString(3, user.getName());
      pstmt.setInt(4, user.getId());

      pstmt.executeUpdate();
      System.out.println("Профиль пользователя успешно обновлен.");
    } catch (SQLException e) {
      System.out.println("Ошибка при обновлении пользователя: " + e.getMessage());
    }
  }

  /**
   * Resets a user's password.
   *
   * @param email the email address of the user
   */
  public void resetPassword(String email) {
    String newPassword = generatePassword();
    User user = getUserByEmail(email);
    if (user != null) {
      user.setPassword(newPassword);
      System.out.println("Новый пароль: " + newPassword);
    } else {
      System.out.println("Пользователь с email " + email + " не найден");
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
   * Удалить пользователя по email.
   *
   * @param email email пользователя
   */
  public void deleteUser(String email) {
    String sql = "DELETE FROM postgres_schema.users WHERE email = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, email);

      int affectedRows = pstmt.executeUpdate();
      if (affectedRows > 0) {
        System.out.println("Пользователь с email " + email + " успешно удален.");
      } else {
        System.out.println("Пользователь с email " + email + " не найден.");
      }

    } catch (SQLException e) {
      System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
    }
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }
}
