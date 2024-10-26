package org.example.grudapp.repository;

import static org.example.grudapp.repository.DatabaseConstants.PASSWORD;
import static org.example.grudapp.repository.DatabaseConstants.URL;
import static org.example.grudapp.repository.DatabaseConstants.USERNAME;

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

public class LogRepository {

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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.CREATE_LOG)) {

      preparedStatement.setDate(1, new java.sql.Date(logDate.getTime()));
      preparedStatement.setBoolean(2, completed);
      preparedStatement.setInt(3, habit.getId());
      preparedStatement.setInt(4, user.getId());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          int generatedId = resultSet.getInt(1);
          Log newLog = new Log();
          newLog.setId(generatedId);
          newLog.setLogDate(logDate);
          newLog.setCompleted(completed);
          newLog.setHabit(habit);
          newLog.setUser(user);
          logs.put(generatedId, newLog);
        }
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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.GET_LOGS_BY_USER)) {

      preparedStatement.setInt(1, user.getId());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Log log = new Log();
          log.setId(resultSet.getInt("id"));
          log.setLogDate(resultSet.getDate("log_date"));
          log.setCompleted(resultSet.getBoolean("completed"));
          log.setHabit(new Habit(resultSet.getInt("habit_id")));
          log.setUser(user);
          userLogs.add(log);
        }
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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.UPDATE_LOG)) {

      preparedStatement.setDate(1, new java.sql.Date(logDate.getTime()));
      preparedStatement.setBoolean(2, completed);
      preparedStatement.setInt(3, logId);

      preparedStatement.executeUpdate();

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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.DELETE_LOG)) {

      preparedStatement.setInt(1, logId);

      preparedStatement.executeUpdate();

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
    int maxStreak = 0;
    int streak = 0;
    Date previousLogDate = null;

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.GET_STREAK)) {

      preparedStatement.setInt(1, habit.getId());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
    int totalLogs = 0;
    int successfulLogs = 0;

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.GET_SUCCESS_PERCENTAGE)) {

      preparedStatement.setInt(1, habit.getId());
      preparedStatement.setDate(2, new java.sql.Date(startDate.getTime()));
      preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()));

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.ADD_LOG)) {

      preparedStatement.setDate(1, new java.sql.Date(log.getLogDate().getTime()));
      preparedStatement.setBoolean(2, log.isCompleted());
      preparedStatement.setInt(3, log.getHabit().getId());
      preparedStatement.setInt(4, log.getUser().getId());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          int generatedId = resultSet.getInt(1);
          log.setId(generatedId);
          logs.put(generatedId, log);
        }
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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.GET_LOGS_BY_HABIT)) {

      preparedStatement.setInt(1, habit.getId());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Log log = new Log();
          log.setId(resultSet.getInt("id"));
          log.setLogDate(resultSet.getDate("log_date"));
          log.setCompleted(resultSet.getBoolean("completed"));
          log.setHabit(habit);
          log.setUser(new User(resultSet.getInt("user_id")));
          logsByHabit.add(log);
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения логов по привычке: " + e.getMessage());
    }
    return logsByHabit;
  }
}
