package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
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
  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  // Set of all admins and users
  private Set<User> admins = new HashSet<>();
  private Set<User> users = new HashSet<>();
  private Set<Habit> habits = new HashSet<>();

  // Get all habits
  public Set<Habit> getHabits() {
    return habits;
  }

  // Constructor
  public AdminService(Set<User> admins, Set<User> users, Set<Habit> habits) {
    this.admins = admins;
    this.users = users;
    this.habits = habits;
  }

  public AdminService() {
  }

  // Getters and Setters
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

  // Add user method
  public void addUser(User user) {
    admins.add(user);
    users.add(user);
    saveUser(user);
  }

  // Remove user method
  public void removeUser(User user) {
    admins.remove(user);
    users.remove(user);
    deleteUser(user);
  }

  // Save user to the database
  private void saveUser(User user) {
    String query = "INSERT INTO users (email, password, name, role) VALUES (?, ?, ?, ?)";
    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getEmail());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getName());
      statement.setString(4, user.getRole().toString());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error saving user: " + e.getMessage());
    }
  }

  // Delete user from the database
  private void deleteUser(User user) {
    String query = "DELETE FROM users WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error deleting user: " + e.getMessage());
    }
  }

  // Assign admin role
  public void assignAdminRole(User user) {
    user.setRole(Role.ADMIN);
    admins.add(user);
    updateUserRole(user);
  }

  // Assign user role
  public void assignUserRole(User user) {
    user.setRole(Role.USER);
    admins.remove(user);
    updateUserRole(user);
  }

  // Update user role in the database
  private void updateUserRole(User user) {
    String query = "UPDATE users SET role = ? WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, user.getRole().toString());
      statement.setInt(2, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error updating user role: " + e.getMessage());
    }
  }

  // Add habit method
  public void addHabit(Habit habit) {
    habits.add(habit);
    saveHabit(habit);
  }

  // Remove habit method
  public void removeHabit(Habit habit) {
    habits.remove(habit);
    deleteHabit(habit.getId());
  }

  // Save habit to the database
  private void saveHabit(Habit habit) {
    String query = "INSERT INTO habits (name, description) VALUES (?, ?)";
    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, habit.getName());
      statement.setString(2, habit.getDescription());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error saving habit: " + e.getMessage());
    }
  }

  // Delete habit from the database
  private void deleteHabit(int id) {
    String query = "DELETE FROM habits WHERE id = ?";
    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, id);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error deleting habit: " + e.getMessage());
    }
  }
}
