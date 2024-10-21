package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;

/**
 * Provides services for managing users.
 */
public class UserService {

  /**
   * The currently authenticated user.
   */
  private User authenticatedUser;
  private Connection connection;
  private Set<User> admins = new HashSet<>();


  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  /**
   * Returns a database connection. If a connection has been previously injected, it is returned.
   * Otherwise, a new connection is created using the provided URL, username, and password.
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

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public Set<User> getAdmins() {
    return admins;
  }


  /**
   * Retrieves a map of all users from the database.
   *
   * @return A map where the keys are user IDs and the values are corresponding User objects.
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
        user.setRole(Role.valueOf(rs.getString("role")));
        users.put(user.getId(), user);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при получении пользователей: " + e.getMessage());
    }
    return users;
  }

  /**
   * Returns the authorized user by his email.
   * <p>
   * This method runs an SQL query to look up a user in a database based on their email. If the user
   * is found and authorized, it is returned as a User object.
   *
   * @param email user's email
   * @return the authorized user, or null if the user is not found or not authorized
   */
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
   * New user registration.
   *
   * @param email    user's email
   * @param password user password
   * @param name     username
   */
  public User registerUser(String email, String password, String name) {
    if (getUserByEmail(email) != null) {
      System.out.println(
          "Пользователь с таким email уже существует. Пожалуйста, введите другой email.");
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

  /**
   * Returns the currently authenticated user.
   *
   * @return the authenticated user, or null if the user is not authenticated
   */
  public User getAuthenticatedUser() {
    return authenticatedUser;
  }

  /**
   * Authenticates the user by email and password.
   * <p>
   * This method runs an SQL query to look up a user in a database based on their email and
   * password. If the user is found and the password matches, it is returned as an authenticated
   * user.
   *
   * @param email    user's email
   * @param password user password
   * @return the authenticated user, or null if the user is not found or the password is incorrect
   */
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
   * Returns the user by ID.
   * <p>
   * This method runs an SQL query to look up a user in the database based on their user ID. If the
   * user is found, it is returned as a User object.
   *
   * @param id user identifier
   * @return the user, or null if the user is not found
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

  /**
   * Updates the user's profile in the database.
   * <p>
   * This method accepts a User object containing the updated user data, and updates the
   * corresponding entry in the users table of the database.
   *
   * @param user a User object containing the updated user data
   */
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
   * Resets the user's password via his email.
   * <p>
   * This method generates a new password, finds the user by his email, and updates user password if
   * the user is found.
   *
   * @param email email of the user for whom you want to reset the password
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
   * Generates a random password of 8 characters.
   *
   * @return A string representing the random password.
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
   * Removes a user from the database by his email.
   *
   * @param email email of the user to be deleted
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

  /**
   * Assigns the ADMIN role to a user and adds them to the collection of admins.
   *
   * @param user The user object to assign the ADMIN role to.
   */
  public void assignAdminRole(User user) {
    user.setRole(Role.ADMIN);
    admins.add(user);
    updateUserRole(user);
  }

  /**
   * Assigns the USER role to a user and removes them from the collection of admins.
   *
   * @param user The user object to assign the USER role to.
   */
  public void assignUserRole(User user) {
    user.setRole(Role.USER);
    admins.remove(user);
    updateUserRole(user);
  }

  /**
   * Updates the role of a user in the database.
   *
   * @param user The user object to update the role for.
   */
  public void updateUserRole(User user) {
    String sql = "UPDATE postgres_schema.users SET role = ? WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, user.getRole().toString());
      pstmt.setInt(2, user.getId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error updating user role: " + e.getMessage());
    }
  }
}
