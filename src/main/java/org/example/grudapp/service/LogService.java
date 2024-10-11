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
}
