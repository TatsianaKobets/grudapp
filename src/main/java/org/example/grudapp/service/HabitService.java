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

  private Map<Integer, Habit> habits = new HashMap<>();
  public Map<Integer, Habit> getHabits() {
    return habits;
  }

  private static final String URL = "jdbc:postgresql://postgres:5432/postgres?currentSchema=postgres_schema";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  /**
   * Creates a new habit for the specified user.
   *
   * @param name        the name of the habit
   * @param description the description of the habit
   * @param frequency   the frequency of the habit
   * @param user        the user who owns the habit
   *
   * @throws SQLException  if an error occurs while executing the database query
   */
  public void createHabit(String name, String description, String frequency, User user) {
    String sql = "INSERT INTO postgres_schema.habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, name);
      pstmt.setString(2, description);
      pstmt.setString(3, frequency);
      pstmt.setLong(4, user.getId());
      pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        Habit habit = new Habit(generatedId, name, description, frequency, user, new Date());
        user.getHabits().add(habit);
      }

    } catch (SQLException e) {
      System.out.println("Ошибка при создании привычки: " + e.getMessage());
    }
  }

  /**
   * Retrieves a list of habits for the specified user.
   *
   * @param user  the user for whom to retrieve habits
   *
   * @return a list of habits for the specified user
   *
   * @throws SQLException  if an error occurs while executing the database query
   */
  public List<Habit> getHabitsByUser(User user) {
    List<Habit> result = new ArrayList<>();
    String sql = "SELECT * FROM postgres_schema.habits WHERE user_id = ?";

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
      System.out.println("Ошибка при получении привычек: " + e.getMessage());
    }
    return result;
  }

  /**
   * Updates an existing habit with the specified details.
   *
   * @param habitId  the ID of the habit to update
   * @param name     the new name of the habit
   * @param description  the new description of the habit
   * @param frequency  the new frequency of the habit
   *
   * @throws SQLException  if an error occurs while executing the database query
   */
  public void updateHabit(int habitId, String name, String description, String frequency) {
    String sql = "UPDATE postgres_schema.habits SET name = ?, description = ?, frequency = ?, creation_date = ? WHERE id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, name);
      pstmt.setString(2, description);
      pstmt.setString(3, frequency);
      pstmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.setInt(5, habitId);

      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка при обновлении привычки: " + e.getMessage());
    }
  }

  /**
   * Deletes a habit with the specified ID from the database.
   *
   * @param habitId  the ID of the habit to delete
   *
   * @throws SQLException  if an error occurs while executing the database query
   */
  public void deleteHabit(int habitId) {
    String sql = "DELETE FROM postgres_schema.habits WHERE id = ?";

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
                  pstmtDelete.setInt(1, habitId);
                  pstmtDelete.executeUpdate();
                  System.out.println("Привычка успешно удалена.");
              } else {
                  System.out.println("У вас нет разрешения на удаление этой привычки.");
              }
          } else {
              System.out.println("Привычка не обнаружена.");
          }

      } catch (SQLException e) {
          System.out.println("Ошибка при удалении привычки: " + e.getMessage());
      }
  }*/

  /**
   * Saves a new habit to the database.
   *
   * @param habit  the habit to be saved
   *
   * @throws SQLException  if an error occurs while executing the database query
   */
  public void saveHabit(Habit habit) {
    String sql = "INSERT INTO postgres_schema.habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, habit.getName());
      pstmt.setString(2, habit.getDescription());
      pstmt.setString(3, habit.getFrequency());
      pstmt.setLong(4,
          habit.getUser().getId());
      pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));

      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        habit.setId(generatedId);
        habit.getUser().getHabits().add(habit);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка сохранения привычки: " + e.getMessage());
    }
  }
}
