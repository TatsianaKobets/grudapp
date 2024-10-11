package org.example.grudapp.service;

import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Log;
import org.example.grudapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LogServiceTest {

  private LogService logService;
  private User user;
  private Habit habit;

  @BeforeEach
  void setUp() {
    logService = new LogService();
    user = new User(1, "John Doe", "johndoe", "password123");
    habit = new Habit(1, "Test Habit", "This is a test habit", "daily", user, new Date());
  }

  @Test
  void createLog() {
    Date logDate = new Date();
    logService.createLog(logDate, true, habit, user);
    List<Log> logs = logService.getLogs();
    assertEquals(1, logs.size());
    Log log = logs.get(0);
    assertEquals(logDate, log.getLogDate());
    assertEquals(true, log.isCompleted());
    assertEquals(habit, log.getHabit());
    assertEquals(user, log.getUser());
  }

  @Test
  void updateLog() {
    Date logDate = new Date();
    logService.createLog(logDate, true, habit, user);
    List<Log> logs = logService.getLogs();
    Log log = logs.get(0);
    logService.updateLog(log.getId(), new Date(), false);
    logs = logService.getLogs();
    log = logs.get(0);
    assertEquals(new Date(), log.getLogDate());
    assertEquals(false, log.isCompleted());
  }

  @Test
  void deleteLog() {
    Date logDate = new Date();
    logService.createLog(logDate, true, habit, user);
    List<Log> logs = logService.getLogs();
    assertEquals(1, logs.size());
    logService.deleteLog(logs.get(0).getId());
    logs = logService.getLogs();
    assertEquals(0, logs.size());
  }

  @Test
  void getStreak() {
    Date logDate = new Date();
    logService.createLog(logDate, true, habit, user);
    logService.createLog(new Date(logDate.getTime() + 86400000), true, habit, user);
    logService.createLog(new Date(logDate.getTime() + 172800000), true, habit, user);
    assertEquals(3, logService.getStreak(habit));
  }

  @Test
  void getSuccessPercentage() {
    Date logDate = new Date();
    logService.createLog(logDate, true, habit, user);
    logService.createLog(new Date(logDate.getTime() + 86400000), false, habit, user);
    logService.createLog(new Date(logDate.getTime() + 172800000), true, habit, user);
    assertEquals(50.0,
        logService.getSuccessPercentage(habit, logDate, new Date(logDate.getTime() + 259200000)),
        0.01);
  }
  @Test
  void addLog() {
    Log log = new Log(1, new Date(), true, habit, user);
    logService.addLog(log);
    assertEquals(1, logService.getLogs().size());
  }
  @Test
  void getLogsByHabit() {
    Log log1 = new Log(1, new Date(), true, habit, user);
    Log log2 = new Log(2, new Date(), true, habit, user);
    logService.addLog(log1);
    logService.addLog(log2);

    List<Log> logsByHabit = logService.getLogsByHabit(habit);
    assertEquals(2, logsByHabit.size());
    assertEquals(log1, logsByHabit.get(0));
    assertEquals(log2, logsByHabit.get(1));
  }
}
