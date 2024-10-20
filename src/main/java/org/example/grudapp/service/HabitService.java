package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

/**
 * Provides services for managing habits.
 */
public class HabitService {

  /**
   * Map of all habits. The key is the ID of the habit, and the value is the habit.
   */
  private Map<Integer, Habit> habits = new HashMap<>();

  /**
   * Map of all users. The key is the ID of the user, and the value is the user.
   *
   * @return
   */
  public Map<Integer, Habit> getHabits() {
    return habits;
  }

  public void setHabits(Map<Integer, Habit> habits) {
    this.habits = habits;
  }

  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  // Method to create a new habit
  public void createHabit(String name, String description, String frequency, User user) {
    String sql = "INSERT INTO habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, name);
      pstmt.setString(2, description);
      pstmt.setString(3, frequency);
      pstmt.setLong(4, user.getId()); // Assuming User object has getId() method
      pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        Habit habit = new Habit(generatedId, name, description, frequency, user, new Date());
        user.getHabits().add(habit); // Add habit to user habits list
      }

    } catch (SQLException e) {
      System.out.println("Error while creating habit: " + e.getMessage());
    }
  }

  // Get all habits for a user
  public List<Habit> getHabitsByUser(User user) {
    List<Habit> result = new ArrayList<>();
    String sql = "SELECT * FROM habits WHERE user_id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setLong(1, user.getId());
      ResultSet rs = pstmt.executeQuery();

      while (rs.next()) {
        Habit habit = new Habit(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getString("frequency"),
            user,
            rs.getTimestamp("creation_date")
        );
        result.add(habit);
      }
    } catch (SQLException e) {
      System.out.println("Error while fetching habits: " + e.getMessage());
    }

    return result;
  }

  // Update an existing habit
  public void updateHabit(int habitId, String name, String description, String frequency) {
    String sql = "UPDATE habits SET name = ?, description = ?, frequency = ?, creation_date = ? WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, name);
      pstmt.setString(2, description);
      pstmt.setString(3, frequency);
      pstmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.setInt(5, habitId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while updating habit: " + e.getMessage());
    }
  }

  /**
   * Deletes an existing habit.
   *
   * @param habitId the ID of the habit to delete
   */
  // Delete an existing habit
  public void deleteHabit(int habitId) {
    String sql = "DELETE FROM habits WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setInt(1, habitId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while deleting habit: " + e.getMessage());
    }
  }

  /*ToDo
  * public void deleteHabit(int habitId, int userId, String userRole) {
      String sqlSelect = "SELECT user_id FROM habits WHERE id = ?";
      String sqlDelete = "DELETE FROM habits WHERE id = ?";

      try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
           PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect);
           PreparedStatement pstmtDelete = conn.prepareStatement(sqlDelete)) {

          pstmtSelect.setInt(1, habitId);
          ResultSet rs = pstmtSelect.executeQuery();

          if (rs.next()) {
              int habitOwnerId = rs.getInt("user_id");

              if ("admin".equalsIgnoreCase(userRole) || habitOwnerId == userId) {
                  // User is an admin or the owner of the habit
                  pstmtDelete.setInt(1, habitId);
                  pstmtDelete.executeUpdate();
                  System.out.println("Habit deleted successfully.");
              } else {
                  System.out.println("You do not have permission to delete this habit.");
              }
          } else {
              System.out.println("Habit not found.");
          }

      } catch (SQLException e) {
          System.out.println("Error while deleting habit: " + e.getMessage());
      }
  }*/
  public void saveHabit(Habit habit) {
    String sql = "INSERT INTO habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, habit.getName());
      pstmt.setString(2, habit.getDescription());
      pstmt.setString(3, habit.getFrequency());
      pstmt.setLong(4,
          habit.getUser().getId()); // Предполагая, что у объекта Habit есть метод getUser()
      pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        habit.setId(generatedId); // Предполагая, что у объекта Habit есть метод setId()
        habit.getUser().getHabits().add(habit); // Добавляем привычку в список привычек пользователя
      }
    } catch (SQLException e) {
      System.out.println("Ошибка сохранения привычки: " + e.getMessage());
    }
  }
}
