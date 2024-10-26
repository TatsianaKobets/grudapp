package org.example.grudapp.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Log;
import org.example.grudapp.model.User;
import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
import org.example.grudapp.service.UserService;

public class LogManagementModule {
  private static UserService userService;
  private static HabitService habitService;
  private static LogService logService;



  static void createLog(Scanner scanner, User currentUser) {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();
    scanner.nextLine();
    System.out.println("Введите дату выполнения (например yyyy-MM-dd):");
    String logDateString = scanner.nextLine();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date logDate;
    try {
      logDate = sdf.parse(logDateString);
    } catch (ParseException e) {
      System.out.println("Ошибка: некорректная дата. " + e.getMessage());
      return;
    }
    System.out.println("Введите выполнено ли задание (true/false):");
    boolean completed = scanner.nextBoolean();

    List<Habit> habits = habitService.getHabitsByUser(currentUser);

    Habit habit = habits.stream()
        .filter(h -> h.getId() == habitId)
        .findFirst()
        .orElse(null);

    if (habit == null) {
      System.out.println("Некорректный ID привычки. Пожалуйста, введите корректный ID.");
      return;
    }
    logService.createLog(logDate, completed, habit, currentUser);
    System.out.println("Лог успешно добавлен.");
  }
  static void viewLogs(User currentUser) {
    if (currentUser == null) {
      System.out.println("Ошибка: пользователь не авторизован.");
      return;
    }

    List<Log> logs = logService.getLogsByUser(currentUser);
    if (logs.isEmpty()) {
      System.out.println("У вас нет отметок.");
      return;
    }
    for (Log log : logs) {
      System.out.println("Дата выполнения: " + log.getLogDate());
      System.out.println("Название привычки: " + log.getHabit().getName());
      System.out.println("Выполнено: " + (log.isCompleted() ? "Да" : "Нет"));
      System.out.println("Создатель: " + log.getUser().getName());
      System.out.println();
    }
  }

  static void deleteLog(Scanner scanner) {
    System.out.println("Введите ID отметки:");
    int logId = scanner.nextInt();
    logService.deleteLog(logId);
  }
}
