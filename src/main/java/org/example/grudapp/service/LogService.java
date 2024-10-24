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

  private static final String URL = "jdbc:postgresql://postgres:5432/postgres?currentSchema=postgres_schema";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  private Map<Integer, Log> logs = new HashMap<>();


  /**
   * Creates a new log entry in the database.
   *
   * @param logDate   the date of the log entry
   * @param completed whether the habit was completed on this date
   * @param habit     the habit associated with this log entry
   * @param user      the user who owns this log entry
   * @throws SQLException if an error occurs while executing the database query
   */
  public void createLog(Date logDate, boolean completed, Habit habit, User user) {
    String query = "INSERT INTO postgres_schema.logs (log_date, completed, habit_id, user_id) VALUES (?, ?, ?, ?) RETURNING id";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      statement.setDate(1, new java.sql.Date(logDate.getTime()));
      statement.setBoolean(2, completed);
      statement.setInt(3, habit.getId());
      statement.setInt(4, user.getId());

      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        Log newLog = new Log();
        newLog.setId(generatedId);
        newLog.setLogDate(logDate);
        newLog.setCompleted(completed);
        newLog.setHabit(habit);
        newLog.setUser(user);
        logs.put(generatedId, newLog);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка создания логов: " + e.getMessage());
    }
  }

  /**
   * Retrieves a list of log entries for a given user.
   *
   * @param user the user whose log entries are to be retrieved
   * @return a list of log entries for the specified user
   * @throws SQLException if an error occurs while executing the database query
   */
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
        log.setHabit(
            new Habit(resultSet.getInt("habit_id"))); // Предполагаю, что вы имеете этот конструктор
        log.setUser(user);
        userLogs.add(log);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения логов пользователя: " + e.getMessage());
    }

    return userLogs;
  }

  /**
   * Updates a log entry with the specified ID.
   *
   * @param logId     the ID of the log entry to update
   * @param logDate   the new log date
   * @param completed the new completion status
   * @throws SQLException if an error occurs while executing the database query
   */
  public void updateLog(int logId, Date logDate, boolean completed) {
    String query = "UPDATE postgres_schema.logs SET log_date = ?, completed = ? WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {

      statement.setDate(1, new java.sql.Date(logDate.getTime()));
      statement.setBoolean(2, completed);
      statement.setInt(3, logId);
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка обновления логов: " + e.getMessage());
    }
  }

  /**
   * Deletes a log entry with the specified ID.
   *
   * @param logId the ID of the log entry to delete
   * @throws SQLException if an error occurs while executing the database query
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
   * Returns the maximum streak of consecutive days for which the specified habit was completed.
   *
   * @param habit the habit for which to calculate the streak
   * @return the maximum streak of consecutive days for which the habit was completed
   * @throws SQLException if an error occurs while executing the database query
   */
  public int getStreak(Habit habit) {
    String query = "SELECT * FROM postgres_schema.logs WHERE habit_id = ? ORDER BY log_date";
    int maxStreak = 0;
    int streak = 0;
    Date previousLogDate = null;

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
              streak = 1;
            }
          } else {
            streak = 1;
          }
          maxStreak = Math.max(maxStreak, streak);
        }
        previousLogDate = logDate;
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения стика: " + e.getMessage());
    }

    return maxStreak;
  }

  /**
   * Returns the percentage of successful log entries for the specified habit within the given date
   * range.
   *
   * @param habit     the habit for which to calculate the success percentage
   * @param startDate the start date of the date range (inclusive)
   * @param endDate   the end date of the date range (inclusive)
   * @return the percentage of successful log entries for the habit within the date range (as a
   * double value between 0.0 and 100.0)
   * @throws SQLException if an error occurs while executing the database query
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

  /**
   * Adds a new log entry to the database.
   *
   * @param log the log entry to be added
   * @throws SQLException if an error occurs while executing the database query
   */
  public void addLog(Log log) {
    String query = "INSERT INTO postgres_schema.logs (log_date, completed, habit_id, user_id) VALUES (?, ?, ?, ?) RETURNING id";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setDate(1, new java.sql.Date(log.getLogDate().getTime()));
      statement.setBoolean(2, log.isCompleted());
      statement.setInt(3, log.getHabit().getId());
      statement.setInt(4, log.getUser().getId());

      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        int generatedId = rs.getInt(1);
        log.setId(generatedId);
        logs.put(generatedId, log);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка добавления логов: " + e.getMessage());
    }
  }

  /**
   * Retrieves a list of log entries for the specified habit.
   *
   * @param habit the habit for which to retrieve log entries
   * @return a list of log entries for the specified habit
   * @throws SQLException if an error occurs while executing the database query
   */
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