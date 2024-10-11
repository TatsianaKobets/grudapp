package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Log;
import org.example.grudapp.model.User;

public class LogService {

  private List<Log> logs = new ArrayList<>();

  public List<Log> getLogs() {
    return logs;
  }

  public void createLog(Date logDate, boolean completed, Habit habit, User user) {
    Log log = new Log(logs.size() + 1, logDate, completed, habit, user);
    logs.add(log);
    habit.getLogs().add(log);
  }

  public void updateLog(int logId, Date logDate, boolean completed) {
    for (Log log : logs) {
      if (log.getId() == logId) {
        log.setLogDate(logDate);
        log.setCompleted(completed);
        return;
      }
    }
  }

  public void deleteLog(int logId) {
    for (Log log : logs) {
      if (log.getId() == logId) {
        logs.remove(log);
        return;
      }
    }
  }
  public int getStreak(Habit habit) {
    int streak = 0;
    int maxStreak = 0;
    Date previousLogDate = null;

    List<Log> logs = new ArrayList<>(habit.getLogs());
    logs.sort((log1, log2) -> log1.getLogDate().compareTo(log2.getLogDate()));

    for (Log log : logs) {
      if (log.isCompleted()) {
        if (previousLogDate != null) {
          long diffInDays = (log.getLogDate().getTime() - previousLogDate.getTime()) / (1000 * 60 * 60 * 24);
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
  public List<Log> getLogsByHabit(Habit habit) {
    List<Log> logs = new ArrayList<>();
    for (Log log : logs) {
      if (log.getHabit().equals(habit)) {
        logs.add(log);
      }
    }
    return logs;
  }
}
