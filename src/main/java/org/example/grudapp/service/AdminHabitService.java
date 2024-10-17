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

public class AdminHabitService {

  private Connection connection;

  public AdminHabitService() {
    try {
      connection = DatabaseConnector.getConnection();
    } catch (SQLException e) {
      System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
    }
  }

  /**
   * Map of all habits in the system. The key is the ID of the habit, and the value is the habit.
   */
  private Map<Integer, Habit> habitsInSystem = new HashMap<>();

  /**
   * Map of all users in the system. The key is the ID of the user, and the value is the user.
   *
   * @return
   */
  public Map<Integer, Habit> getHabitsInSystem() {
    return habitsInSystem;
  }

  public void setHabitsInSystem(Map<Integer, Habit> habitsInSystem) {
    this.habitsInSystem = habitsInSystem;
  }

  /**
   * Creates a new habit for from the role administrator in the system.
   *
   * @param name        the name of the habit
   * @param description the description of the habit
   */
  public void createHabitInSystem(String name, String description) {
    String query = "INSERT INTO habits (name, description) VALUES (?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, name);
      statement.setString(2, description);
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
}
