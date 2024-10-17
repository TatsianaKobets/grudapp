package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.grudapp.dbconnect.DatabaseConnector;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

/**
 * Provides services for managing habits.
 */
public class HabitService {

  private Connection connection;

  public HabitService() {
    try {
      connection = DatabaseConnector.getConnection();
    } catch (SQLException e) {
      System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
    }
  }

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

  /**
   * Creates a new habit for a given user.
   *
   * @param name
   * @param description
   * @param frequency
   * @param user
   */
  public void createHabit(String name, String description, String frequency, User user) {
    String query = "INSERT INTO habits (name, description, frequency, user_id) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, name);
      statement.setString(2, description);
      statement.setString(3, frequency);
      statement.setInt(4, user.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка создания привычки: " + e.getMessage());
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
   * Returns the list of habits for a given user.
   *
   * @param user the user to get habits for
   * @return the list of habits for the user
   */
  public List<Habit> getHabitsByUser(User user) {
    List<Habit> habits = new ArrayList<>();
    String query = "SELECT * FROM habits WHERE user_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, user.getId());
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          Habit habit = new Habit();
          habit.setId(resultSet.getInt("id"));
          habit.setName(resultSet.getString("name"));
          habit.setDescription(resultSet.getString("description"));
          habit.setFrequency(resultSet.getString("frequency"));
          habits.add(habit);
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения привычек пользователя: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
    return habits;
  }


  /**
   * Updates an existing habit.
   *
   * @param habitId     the ID of the habit to update
   * @param name        the new name of the habit
   * @param description the new description of the habit
   * @param frequency   the new frequency of the habit
   */
  public void updateHabit(int habitId, String name, String description, String frequency) {
    String query = "UPDATE habits SET name = ?, description = ?, frequency = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, name);
      statement.setString(2, description);
      statement.setString(3, frequency);
      statement.setInt(4, habitId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка обновления привычки: " + e.getMessage());
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
   * Deletes an existing habit.
   *
   * @param habitId the ID of the habit to delete
   */
  public void deleteHabit(int habitId) {
    String query = "DELETE FROM habits WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, habitId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка удаления привычки: " + e.getMessage());
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

  public void saveHabit(Habit habit) {
    String query = "INSERT INTO habits (name, description, frequency, user_id) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, habit.getName());
      statement.setString(2, habit.getDescription());
      statement.setString(3, habit.getFrequency());
      statement.setInt(4, habit.getUser().getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка сохранения привычки: " + e.getMessage());
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
}
