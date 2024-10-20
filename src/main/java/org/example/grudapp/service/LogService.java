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
import org.example.grudapp.model.Log;
import org.example.grudapp.model.User;

/**
 * Provides services for managing logs.
 */
public class LogService {

  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";
  /**
   * Map of all logs. The key is the ID of the log, and the value is the log.
   */
  private Map<Integer, Log> logs = new HashMap<>();

  public Map<Integer, Log> getLogs() {
    return logs;
  }

  /**
   * Creates a new log.
   *
   * @param logDate   the date of the log
   * @param completed indicates whether the log is completed
   * @param habit     the habit associated with the log
   * @param user      the user who created the log
   */
  public void createLog(Date logDate, boolean completed, Habit habit, User user) {
    String query = "INSERT INTO postgres_schema.logs (log_date, completed, habit_id, user_id) VALUES (?, ?, ?, ?) RETURNING id";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      // Установка параметров
      statement.setDate(1, new java.sql.Date(logDate.getTime()));
      statement.setBoolean(2, completed);
      statement.setInt(3, habit.getId());
      statement.setInt(4, user.getId());

      // Выполнение запроса
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        // Создаем новый объект Log и добавляем его в локальную коллекцию
        Log newLog = new Log();
        newLog.setId(generatedId);
        newLog.setLogDate(logDate);
        newLog.setCompleted(completed);
        newLog.setHabit(habit);
        newLog.setUser(user);
        logs.put(generatedId, newLog); // Добавляем лог в локальную коллекцию
      }
    } catch (SQLException e) {
      System.out.println("Ошибка создания логов: " + e.getMessage());
    }
  }

  public List<Log> getLogsByUser(User user) {
    List<Log> userLogs = new ArrayList<>();
    String query = "SELECT * FROM postgres_schema.logs WHERE user_id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      statement.setInt(1, user.getId());
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Log log = new Log();
        log.setId(resultSet.getInt("id"));
        log.setLogDate(resultSet.getDate("log_date"));
        log.setCompleted(resultSet.getBoolean("completed"));
        log.setHabit(new Habit(resultSet.getInt("habit_id"))); // Предполагаю, что вы имеете этот конструктор
        log.setUser(user);
        userLogs.add(log);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения логов пользователя: " + e.getMessage());
    }

    return userLogs;
  }
  /**
   * Updates an existing log.
   *
   * @param logId     the ID of the log to update
   * @param logDate   the new date of the log
   * @param completed the new completion status of the log
   */
  public void updateLog(int logId, Date logDate, boolean completed) {
    String query = "UPDATE postgres_schema.logs SET log_date = ?, completed = ? WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {
      // Установка параметров
      statement.setDate(1, new java.sql.Date(logDate.getTime()));
      statement.setBoolean(2, completed);
      statement.setInt(3, logId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка обновления логов: " + e.getMessage());
    }
  }

  /**
   * Deletes an existing log.
   *
   * @param logId the ID of the log to delete
   */
  public void deleteLog(int logId) {
    String query = "DELETE FROM postgres_schema.logs WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setInt(1, logId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка удаления логов: " + e.getMessage());
    }
  }

  /**
   * Calculates the longest streak of completed logs for a given habit.
   *
   * @param habit the habit to calculate the streak for
   * @return the longest streak of completed logs
   */
  public int getStreak(Habit habit) {
    String query = "SELECT * FROM postgres_schema.logs WHERE habit_id = ? ORDER BY log_date";
    int maxStreak = 0;
    int streak = 0;
    Date previousLogDate = null;

    // Используем try-with-resources для автоматического управления соединением
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      statement.setInt(1, habit.getId());
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Date logDate = resultSet.getDate("log_date");
        boolean completed = resultSet.getBoolean("completed");

        if (completed) {
          if (previousLogDate != null) {
            long diffInDays =
                (logDate.getTime() - previousLogDate.getTime()) / (1000 * 60 * 60 * 24);
            if (diffInDays == 1) {
              streak++;
            } else {
              streak = 1; // Сбрасываем счетчик при разрыве последовательности
            }
          } else {
            streak = 1; // Первый выполненный лог
          }
          maxStreak = Math.max(maxStreak, streak);
        }
        previousLogDate = logDate; // Обновляем предыдущую дату лога
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения стика: " + e.getMessage());
    }

    return maxStreak; // Возвращаем максимальный стрик
  }

  /**
   * Calculates the success percentage of a habit within a given date range.
   *
   * @param habit     the habit to calculate the success percentage for
   * @param startDate the start date of the range
   * @param endDate   the end date of the range
   * @return the success percentage as a decimal value
   */
  public double getSuccessPercentage(Habit habit, Date startDate, Date endDate) {
    String query = "SELECT * FROM postgres_schema.logs WHERE habit_id = ? AND log_date BETWEEN ? AND ?";
    int totalLogs = 0;
    int successfulLogs = 0;

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      statement.setInt(1, habit.getId());
      statement.setDate(2, new java.sql.Date(startDate.getTime()));
      statement.setDate(3, new java.sql.Date(endDate.getTime()));

      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          boolean completed = resultSet.getBoolean("completed");
          totalLogs++;
          if (completed) {
            successfulLogs++;
          }
        }
      }

    } catch (SQLException e) {
      System.out.println("Ошибка получения процента успеха: " + e.getMessage());
    }

    if (totalLogs == 0) {
      return 0.0;
    }
    return (double) successfulLogs / totalLogs * 100;
  }

  public void addLog(Log log) {
    String query = "INSERT INTO postgres_schema.logs (log_date, completed, habit_id, user_id) VALUES (?, ?, ?, ?) RETURNING id";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      // Set parameters for the prepared statement
      statement.setDate(1, new java.sql.Date(log.getLogDate().getTime()));
      statement.setBoolean(2, log.isCompleted());
      statement.setInt(3, log.getHabit().getId());
      statement.setInt(4, log.getUser().getId());

      // Execute the update and retrieve the generated ID
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        log.setId(generatedId);  // Assuming Log has a method to set its ID
        // Optionally, you can add this log to your logs Map if you maintain it
        logs.put(generatedId, log);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка добавления логов: " + e.getMessage());
    }
  }

  public List<Log> getLogsByHabit(Habit habit) {
    List<Log> logsByHabit = new ArrayList<>();
    String query = "SELECT * FROM postgres_schema.logs WHERE habit_id = ?";

    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      statement.setInt(1, habit.getId());
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Log log = new Log();
        log.setId(resultSet.getInt("id"));
        log.setLogDate(resultSet.getDate("log_date"));
        log.setCompleted(resultSet.getBoolean("completed"));
        log.setHabit(habit);
        log.setUser(new User(resultSet.getInt("user_id")));
        logsByHabit.add(log);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения логов по привычке: " + e.getMessage());
    }

    return logsByHabit;
  }
}