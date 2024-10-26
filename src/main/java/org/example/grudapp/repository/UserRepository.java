package org.example.grudapp.repository;

import static org.example.grudapp.repository.DatabaseConstants.PASSWORD;
import static org.example.grudapp.repository.DatabaseConstants.URL;
import static org.example.grudapp.repository.DatabaseConstants.USERNAME;

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

public class UserRepository {

  private User authenticatedUser;
  private Set<User> admins = new HashSet<>();

  /**
   * Retrieves a map of all users from the database.
   *
   * @return A map where the keys are user IDs and the values are corresponding User objects.
   */
  public Map<Integer, User> getUsers() {
    Map<Integer, User> users = new HashMap<>();

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.GET_USERS)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          User user = new User(
              resultSet.getInt("id"),
              resultSet.getString("email"),
              resultSet.getString("password"),
              resultSet.getString("name")
          );
          user.setRole(Role.valueOf(resultSet.getString("role")));
          users.put(user.getId(), user);
        }
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
    User user = null;

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.GET_USER_BY_EMAIL)) {

      preparedStatement.setString(1, email);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          user = new User(
              resultSet.getInt("id"),
              resultSet.getString("email"),
              resultSet.getString("password"),
              resultSet.getString("name")
          );
          user.setRole(Role.valueOf(resultSet.getString("role")));
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при получении пользователя по email: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Ошибка при преобразовании роли: " + e.getMessage());
    }
    return user;
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
    User user = null;

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.GET_USER_BY_ID)) {

      preparedStatement.setInt(1, id);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          user = new User(
              resultSet.getInt("id"),
              resultSet.getString("email"),
              resultSet.getString("password"),
              resultSet.getString("name")
          );
          user.setRole(Role.valueOf(resultSet.getString("role")));
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при получении пользователя: " + e.getMessage());
    }
    return user;
  }

  public void createUser(User user) {

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.CREATE_USER)) {

      preparedStatement.setString(1, user.getEmail());
      preparedStatement.setString(2, user.getPassword());
      preparedStatement.setString(3, user.getName());

      preparedStatement.executeUpdate();

    } catch (SQLException e) {
      System.out.println("Ошибка при создании пользователя: " + e.getMessage());
    }
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
    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.EDIT_USER_PROFILE)) {

      preparedStatement.setString(1, user.getEmail());
      preparedStatement.setString(2, user.getPassword());
      preparedStatement.setString(3, user.getName());
      preparedStatement.setInt(4, user.getId());

      preparedStatement.executeUpdate();

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

    Role role = getUsers().isEmpty() ? Role.ADMIN : Role.USER;

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {

      preparedStatement.setString(1, email);
      preparedStatement.setString(2, password);
      preparedStatement.setString(3, name);
      preparedStatement.setString(4, role.toString());

      preparedStatement.executeUpdate();
      if (preparedStatement.executeUpdate() == 0) {
        System.out.println("Не удалось создать пользователя. Попробуйте еще раз.");
        return null;
      }

      try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
        if (resultSet.next()) {
          int userId = resultSet.getInt(1);
          User newUser = new User(userId, email, password, name, role);
          System.out.println(
              "Пользователь зарегистрирован успешно: имя " + name + ", email " + email);
          return newUser;
        } else {
          System.out.println("Не удалось получить ID созданного пользователя.");
          return null;
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при аутентификации пользователя: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
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
    User authenticatedUser = null;

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.AUTHENTICATE_USER)) {

      preparedStatement.setString(1, email);
      preparedStatement.setString(2, password);

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          authenticatedUser = new User(
              resultSet.getInt("id"),
              resultSet.getString("email"),
              resultSet.getString("password"),
              resultSet.getString("name")
          );
          authenticatedUser.setRole(Role.valueOf(resultSet.getString("role")));
          this.authenticatedUser = authenticatedUser;
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при аутентификации пользователя: " + e.getMessage());
    }
    return authenticatedUser;
  }

  /**
   * Removes a user from the database by his email.
   *
   * @param email email of the user to be deleted
   */
  public void deleteUser(String email) {

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.DELETE_USER)) {

      preparedStatement.setString(1, email);

      int affectedRows = preparedStatement.executeUpdate();
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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.UPDATE_USER_ROLE)) {

      preparedStatement.setString(1, user.getRole().toString());
      preparedStatement.setInt(2, user.getId());
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error updating user role: " + e.getMessage());
    }
  }

  /**
   * Retrieves a map of all users with the specified role from the database.
   *
   * @param role the role to filter users by
   * @return A map where the keys are user IDs and the values are corresponding User objects.
   */
  public Map<Integer, User> getUsersByRole(Role role) {
    Map<Integer, User> users = new HashMap<>();

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.GET_USERS_BY_ROLE)) {

      preparedStatement.setString(1, role.toString());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          User user = new User(
              resultSet.getInt("id"),
              resultSet.getString("email"),
              resultSet.getString("password"),
              resultSet.getString("name")
          );
          user.setRole(Role.valueOf(resultSet.getString("role")));
          users.put(user.getId(), user);
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при получении пользователей по роли: " + e.getMessage());
    }
    return users;
  }
}
