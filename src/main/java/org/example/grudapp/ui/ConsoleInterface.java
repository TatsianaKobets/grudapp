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
import org.example.grudapp.service.NotificationService;
import org.example.grudapp.service.UserService;

public class ConsoleInterface {

  private UserService userService;
  private HabitService habitService;
  private LogService logService;
  private NotificationService notificationService;

  public ConsoleInterface(UserService userService, HabitService habitService, LogService logService,
      NotificationService notificationService) {
    this.userService = userService;
    this.habitService = habitService;
    this.logService = logService;
    this.notificationService = notificationService;
  }

  public void run() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("1. Регистрация");
      System.out.println("2. Авторизация");
      System.out.println("3. Создать привычку");
      System.out.println("4. Просмотреть привычки");
      System.out.println("5. Удалить привычку");
      System.out.println("6. Создать отметку о выполнении");
      System.out.println("7. Просмотреть отметки о выполнении");
      System.out.println("8. Удалить отметку о выполнении");
      System.out.println("9. Отправить уведомление");
      System.out.println("10. Выход");

      int choice = scanner.nextInt();

      switch (choice) {
        case 1:
          registerUser(scanner);
          break;
        case 2:
          authenticateUser(scanner);
          break;
        case 3:
          createHabit(scanner);
          break;
        case 4:
          viewHabits();
          break;
        case 5:
          deleteHabit(scanner);
          break;
        case 6:
          createLog(scanner);
          break;
        case 7:
          viewLogs();
          break;
        case 8:
          deleteLog(scanner);
          break;
        case 9:
          sendNotification(scanner);
          break;
        case 10:
          System.exit(0);
          break;
        default:
          System.out.println("Неправильный выбор");
      }
    }
  }// Методы для реализации функций приложения

  private void registerUser(Scanner scanner) {
    System.out.println("Введите имя пользователя:");
    String username = scanner.next();
    System.out.println("Введите электронную почту:");
    String email = scanner.next();
    while (!email.contains("@")) {
      System.out.println("Некорректная электронная почта. Пожалуйста, введите электронную почту с @:");
      email = scanner.next();
    }
    if (userService.getUserByEmail(email) != null) {
      System.out.println("Пользователь с таким email уже существует. Пожалуйста, введите другой email.");
      return;
    }
    System.out.println("Введите пароль:");
    String password = scanner.next();
    userService.registerUser(email, password, username);
  }

  private void authenticateUser(Scanner scanner) {
    System.out.println("Введите электронную почту:");
    String email = scanner.next();
    System.out.println("Введите пароль:");
    String password = scanner.next();
    if (userService.authenticateUser(email, password) != null) {
      System.out.println("Авторизация успешна");
    } else {
      System.out.println("Авторизация неуспешна");
    }
  }

  private void createHabit(Scanner scanner) {
    System.out.println("Введите название привычки:");
    String habitName = scanner.nextLine();
    System.out.println("Введите описание привычки:");
    String habitDescription = scanner.nextLine();
    System.out.println("Введите частоту выполнения:");
    String frequency = scanner.nextLine();
    User user = userService.getAuthenticatedUser();
    habitService.createHabit(habitName, habitDescription, frequency, user);
  }

  private void viewHabits() {
    List<Habit> habits = habitService.getHabits();
    for (Habit habit : habits) {
      System.out.println(habit.getName() + ": " + habit.getDescription());
    }
  }

  private void deleteHabit(Scanner scanner) {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();
    habitService.deleteHabit(habitId);
  }

  private void createLog(Scanner scanner) {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();
    scanner.nextLine(); // Пропускаем остаток строки
    System.out.println("Введите дату выполнения ( например yyyy-MM-dd):");
    // String logDate = scanner.next();
    String logDateString = scanner.nextLine();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date logDate = null;
    try {
      logDate = sdf.parse(logDateString);
    } catch (ParseException e) {
      System.out.println("Ошибка: некорректная дата. " + e.getMessage());
      return;
    }
    System.out.println("Введите выполнено ли задание (true/false):");
    boolean completed = scanner.nextBoolean();

    Habit habit = null;
    for (Habit h : habitService.getHabits()) {
      if (h.getId() == habitId) {
        habit = h;
        break;
      }
    }

    if (habit != null) {
      User user = userService.getAuthenticatedUser();
      logService.createLog(logDate, completed, habit, user);
    } else {
      System.out.println("Привычка не найдена");
    }
  }

  private void viewLogs() {
    List<Log> logs = logService.getLogs();
    for (Log log : logs) {
      /* System.out.println("Дата выполнения: " + log.getLogDate());
    System.out.println("Название привычки: " + log.getHabit().getName());
    System.out.println("Выполнено: " + (log.isCompleted() ? "Да" : "Нет"));
    System.out.println();*/
      System.out.println(log.getHabit().getName() + ": " + log.getLogDate());
    }
  }

  private void deleteLog(Scanner scanner) {
    System.out.println("Введите ID отметки:");
    int logId = scanner.nextInt();
    logService.deleteLog(logId);
  }

  private void sendNotification(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    notificationService.sendNotification(userId);
  }

}