package org.example.grudapp.service;

import java.util.Date;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Log;
import org.example.grudapp.model.User;
import org.example.grudapp.repository.LogRepository;

/**
 * Provides services for managing logs.
 */
public class LogService {


  private LogRepository logRepository;

  public LogService(LogRepository logRepository) {
    this.logRepository = logRepository;
  }

  public void createLog(Date logDate, boolean completed, Habit habit, User user) {
    logRepository.createLog(logDate, completed, habit, user);
  }

  public List<Log> getLogsByUser(User user) {
    return logRepository.getLogsByUser(user);
  }

  public void updateLog(int logId, Date logDate, boolean completed) {
    logRepository.updateLog(logId, logDate, completed);
  }

  public void deleteLog(int logId) {
    logRepository.deleteLog(logId);
  }

  public int getStreak(Habit habit) {
    return logRepository.getStreak(habit);
  }

  public double getSuccessPercentage(Habit habit, Date startDate, Date endDate) {
    return logRepository.getSuccessPercentage(habit, startDate, endDate);
  }

  public void addLog(Log log) {
    logRepository.addLog(log);
  }

  public List<Log> getLogsByHabit(Habit habit) {
    return logRepository.getLogsByHabit(habit);
  }

}