package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import org.example.grudapp.dbconnect.DatabaseConnector;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;

/**
 * Provides services for managing administrators.
 */
public class AdminService {

  HabitService habitService;
  private Connection connection;

  public AdminService() {
    try {
      connection = DatabaseConnector.getConnection();
    } catch (SQLException e) {
      System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
    }
  }

  /**
   * Set of all admins. Initially empty.
   */
  private Set<User> admins = new HashSet<>();
  /**
   * Set of all users.
   */
  private Set<User> users = new HashSet<>();

  public Set<Habit> getHabits() {
    return habits;
  }

  /**
   * Set of all habits.
   */
  private Set<Habit> habits = new HashSet<>();


  public AdminService(Set<User> admins, Set<User> users, Set<Habit> habits) {
    this.admins = admins;
    this.users = users;
    this.habits = habits;
  }

  public Set<User> getAdmins() {
    return admins;
  }

  public void setAdmins(Set<User> admins) {
    this.admins = admins;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public void addUser(User user) {
    admins.add(user);
    users.add(user);
    saveUser(user);
  }

  public void removeUser(User user) {
    admins.remove(user);
    users.remove(user);
    deleteUser(user);
  }

  public void addHabit(Habit habit) {
    habits.add(habit);
    habitService.saveHabit(habit);
  }

  public void removeHabit(Habit habit) {
    habits.remove(habit);
    habitService.deleteHabit(habit.getId());
  }

  /**
   * Assigns the admin role to a user.
   *
   * @param user the user to assign the admin role to
   */
  public void assignAdminRole(User user) {
    user.setRole(Role.ADMIN);
    admins.add(user);
    updateUserRole(user);
  }

  /**
   * Assigns the user role to a user.
   *
   * @param user the user to assign the user role to
   */
  public void assignUserRole(User user) {
    user.setRole(Role.USER);
    admins.remove(user);
    updateUserRole(user);
  }

  private void saveUser(User user) {
    String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getName());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole().toString());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка сохранения пользователя: " + e.getMessage());
    }
  }

  private void deleteUser(User user) {
    String query = "DELETE FROM users WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка удаления пользователя: " + e.getMessage());
    }
  }

  private void updateUserRole(User user) {
    String query = "UPDATE users SET role = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getRole().toString());
      statement.setInt(2, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка обновления роли пользователя: " + e.getMessage());
    }
  }
}
