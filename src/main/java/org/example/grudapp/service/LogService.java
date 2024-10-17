package org.example.grudapp.service;

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
    Log log = new Log(logs.size() + 1, logDate, completed, habit, user);
    logs.put(log.getId(), log);
    habit.getLogs().add(log);
  }

  /**
   * Updates an existing log.
   *
   * @param logId     the ID of the log to update
   * @param logDate   the new date of the log
   * @param completed the new completion status of the log
   */
  public void updateLog(int logId, Date logDate, boolean completed) {
    for (Log log : logs.values()) {
      if (log.getId() == logId) {
        log.setLogDate(logDate);
        log.setCompleted(completed);
        return;
      }
    }
  }

  /**
   * Deletes an existing log.
   *
   * @param logId the ID of the log to delete
   */
  public void deleteLog(int logId) {
    for (Log log : logs.values()) {
      if (log.getId() == logId) {
        logs.remove(log);
        return;
      }
    }
  }

  /**
   * Calculates the longest streak of completed logs for a given habit.
   *
   * @param habit the habit to calculate the streak for
   * @return the longest streak of completed logs
   */
  public int getStreak(Habit habit) {
    int streak = 0;
    int maxStreak = 0;
    Date previousLogDate = null;

    List<Log> logs = new ArrayList<>(habit.getLogs());
    logs.sort((log1, log2) -> log1.getLogDate().compareTo(log2.getLogDate()));

    for (Log log : logs) {
      if (log.isCompleted()) {
        if (previousLogDate != null) {
          long diffInDays =
              (log.getLogDate().getTime() - previousLogDate.getTime()) / (1000 * 60 * 60 * 24);
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
      previousLogDate = log.getLogDate();
    }

    return maxStreak;
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
    List<Log> logs = new ArrayList<>(habit.getLogs());
    logs.sort((log1, log2) -> log1.getLogDate().compareTo(log2.getLogDate()));

    int totalLogs = 0;
    int successfulLogs = 0;

    for (Log log : logs) {
      if (log.getLogDate().after(startDate) && log.getLogDate().before(endDate)) {
        totalLogs++;
        if (log.isCompleted()) {
          successfulLogs++;
        }
      }
    }

    if (totalLogs == 0) {
      return 0.0;
    }

    return (double) successfulLogs / totalLogs * 100;
  }

  public void addLog(Log log) {
    logs.put(log.getId(), log);
  }

  public List<Log> getLogsByHabit(Habit habit) {
    List<Log> logsByHabit = new ArrayList<>();
    for (Log log : logs.values()) {
      if (log.getHabit().equals(habit)) {
        logsByHabit.add(log);
      }
    }
    return logsByHabit;
  }
}
