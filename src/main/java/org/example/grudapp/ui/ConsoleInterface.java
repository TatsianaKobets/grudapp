package org.example.grudapp.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

/**
 * ConsoleInterface is a class that provides a text-based interface for users to interact with the
 * application. It allows users to perform various actions such as registration, authentication,
 * habit creation, log creation, and more.
 */
public class ConsoleInterface {

  private UserService userService;
  private HabitService habitService;
  private LogService logService;
  private NotificationService notificationService;

  /**
   * Constructor for ConsoleInterface.
   *
   * @param userService         UserService instance
   * @param habitService        HabitService instance
   * @param logService          LogService instance
   * @param notificationService NotificationService instance
   */
  public ConsoleInterface(UserService userService, HabitService habitService, LogService logService,
      NotificationService notificationService) {
    this.userService = userService;
    this.habitService = habitService;
    this.logService = logService;
    this.notificationService = notificationService;
  }

  /**
   * Runs the console interface.
   */
  public void run() {
    Scanner scanner = new Scanner(System.in);

    while (true) {
      System.out.println("1. Регистрация");
      System.out.println("2. Авторизация");
      System.out.println("3. Изменить данные пользователя");
      System.out.println("4. Изменить email пользователя");
      System.out.println("5. Удалить пользователя");
      System.out.println("6. Выход");
      System.out.println("7. Создать привычку");
      System.out.println("8. Просмотреть привычки");
      System.out.println("9. Удалить привычку");
      System.out.println("10. Создать отметку о выполнении");
      System.out.println("11. Просмотреть отметки о выполнении");
      System.out.println("12. Удалить отметку о выполнении");
      System.out.println("13. ToDo не реализовано Отправить уведомление");
      System.out.println("14. ToDo не реализовано Сбросить пароль через email");
      System.out.println("15. Администрирование");
      System.out.println("16. просмотреть статистику");
      System.out.println("Выберите действие: ");

      int choice = scanner.nextInt();

      switch (choice) {
        case 1:
          registerUser(scanner);
          break;
        case 2:
          authenticateUser(scanner);
          break;
        case 3:
          editUserProfile(scanner);
          break;
        case 4:
          changeUserEmail(scanner);
          break;
        case 5:
          deleteUser(scanner);
          break;
        case 6:
          System.exit(0);
          break;
        case 7:
          createHabit(scanner);
          break;
        case 8:
          viewHabits(scanner);
          break;
        case 9:
          deleteHabit(scanner);
          break;
        case 10:
          createLog(scanner);
          break;
        case 11:
          viewLogs();
          break;
        case 12:
          deleteLog(scanner);
          break;
        case 13:
          sendNotification(scanner);
          break;
        case 14:
          resetPassword(scanner);
          break;
        case 15:
          adminPanel(scanner);
          break;
        case 16:
          viewStatistics(scanner);
          break;
        default:
          System.out.println("Неправильный выбор");
      }
    }
  }

  /**
   * Registers a new user.
   *
   * @param scanner Scanner instance
   */
  private void registerUser(Scanner scanner) {
    System.out.println("Введите имя пользователя:");
    String username = scanner.next();
    System.out.println("Введите электронную почту:");
    String email = scanner.next();
    while (!email.contains("@")) {
      System.out.println(
          "Некорректная электронная почта. Пожалуйста, введите электронную почту с @:");
      email = scanner.next();
    }
    if (userService.getUserByEmail(email) != null) {
      System.out.println(
          "Пользователь с таким email уже существует. Пожалуйста, введите другой email.");
      return;
    }
    System.out.println("Введите пароль:");
    String password = scanner.next();
    userService.registerUser(email, password, username);
  }

  /**
   * Authenticates a user.
   *
   * @param scanner Scanner instance
   */
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

  /**
   * Edits a user's profile.
   *
   * @param scanner Scanner instance
   */
  private void editUserProfile(Scanner scanner) {
    System.out.println("Введите email пользователя:");
    String email = scanner.next();
    User user = userService.getUserByEmail(email);
    if (user != null) {
      System.out.println("Введите новое имя пользователя:");
      String name = scanner.next();
      System.out.println("Введите новый пароль:");
      String password = scanner.next();
      user.setName(name);
      user.setPassword(password);
      userService.editUserProfile(user);
    } else {
      System.out.println("Пользователь не найден");
    }
  }

  /**
   * Changes a user's email.
   *
   * @param scanner Scanner instance
   */
  private void changeUserEmail(Scanner scanner) {
    System.out.println("Введите текущий email пользователя:");
    String currentEmail = scanner.next();
    User user = userService.getUserByEmail(currentEmail);
    if (user != null) {
      System.out.println("Введите новый email пользователя:");
      String newEmail = scanner.next();
      while (!newEmail.contains("@")) {
        System.out.println(
            "Некорректная электронная почта. Пожалуйста, введите электронную почту с @:");
        newEmail = scanner.next();
      }
      while (userService.getUserByEmail(newEmail) != null) {
        System.out.println(
            "Пользователь с таким email уже существует. Пожалуйста, введите другой email.");
        newEmail = scanner.next();
      }
      user.setEmail(newEmail);
      userService.editUserProfile(user);
      System.out.println("Email пользователя успешно изменен.");
    } else {
      System.out.println("Пользователь не найден");
    }
  }

  /**
   * Deletes a user.
   *
   * @param scanner Scanner instance
   */
  private void deleteUser(Scanner scanner) {
    System.out.println("Введите email пользователя:");
    String email = scanner.next();
    userService.deleteUser(email);
    System.out.println("Пользователь с email " + email + " успешно удален");
  }

  /**
   * Creates a new habit.
   *
   * @param scanner Scanner instance
   */
  private void createHabit(Scanner scanner) {
    User user = userService.getAuthenticatedUser();
    if (user == null) {
      System.out.println("Пожалуйста, авторизуйтесь перед созданием привычки.");
      return;
    }
    System.out.println("Введите название привычки:");
    String habitName = scanner.next();
    System.out.println("Введите описание привычки:");
    String habitDescription = scanner.next();
    System.out.println("Введите частоту выполнения:");
    System.out.println("1. Ежедневно");
    System.out.println("2. Еженедельно");
    int choice = scanner.nextInt();
    String frequency = "";
    switch (choice) {
      case 1:
        frequency = "daily";
        break;
      case 2:
        frequency = "weekly";
        break;
      default:
        System.out.println(
            "Неправильный выбор. Частота выполнения будет установлена как ежедневно.");
        frequency = "daily";
    }

    habitService.createHabit(habitName, habitDescription, frequency, user,
        habitService.getHabits());
  }

  /**
   * Views habits.
   *
   * @param scanner Scanner instance
   */
  private void viewHabits(Scanner scanner) {
    User user = userService.getAuthenticatedUser();
    if (user == null) {
      System.out.println("Пожалуйста, авторизуйтесь перед просмотром привычек.");
      return;
    }

    System.out.println("Выберите фильтр:");
    System.out.println("1. Фильтр по дате создания");
    System.out.println("2. Фильтр по статусу");
    System.out.println("3. Без фильтрации");
    int choice = scanner.nextInt();

    List<Habit> habits = habitService.getHabitsByUser(user);
    if (habits.isEmpty()) {
      System.out.println("У вас нет привычек.");
      return;
    }
    List<Habit> filteredHabits = new ArrayList<>();

    switch (choice) {
      case 1:
        System.out.println("Введите дату создания (например yyyy-MM-dd):");
        String creationDateString = scanner.next();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date creationDate = null;
        try {
          creationDate = sdf.parse(creationDateString);
        } catch (ParseException e) {
          System.out.println("Ошибка: некорректная дата. " + e.getMessage());
          return;
        }
        for (Habit habit : habits) {
          if (habit.getCreationDate().compareTo(creationDate) == 0) {
            filteredHabits.add(habit);
          }
        }
        habits = filteredHabits;
        break;
      case 2:
        System.out.println("Введите статус (true/false):");
        boolean status = scanner.nextBoolean();
        filteredHabits.clear();
        for (Habit habit : habits) {
          if (habit.isCompleted() == status) {
            filteredHabits.add(habit);
          }
        }
        habits = filteredHabits;
        break;
      case 3:
        break;
      default:
        System.out.println("Неправильный выбор. Просмотр привычек без фильтрации.");
    }

    System.out.println("Ваши привычки:");
    for (Habit habit : habits) {
      System.out.println(habit.getId() + ". " + habit.getName() + ": " + habit.getDescription());
    }
  }

  /**
   * Deletes a habit.
   *
   * @param scanner Scanner instance
   */
  private void deleteHabit(Scanner scanner) {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();
    habitService.deleteHabit(habitId);
  }

  /**
   * Creates a new log.
   *
   * @param scanner Scanner instance
   */
  private void createLog(Scanner scanner) {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();
    scanner.nextLine(); // Пропускаем остаток строки

    System.out.println("Введите дату выполнения (например yyyy-MM-dd):");
    String logDateString = scanner.nextLine(); // Читаем дату как строку

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date logDate = null;
    try {
      logDate = sdf.parse(logDateString); // Парсим строку в дату
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

  /**
   * Views logs.
   */
  private void viewLogs() {
    if (logService.getLogs().isEmpty()) {
      System.out.println("У вас нет отметок.");
      return;
    }
    List<Log> logs = logService.getLogs();
    for (Log log : logs) {
      /* System.out.println("Дата выполнения: " + log.getLogDate());
    System.out.println("Название привычки: " + log.getHabit().getName());
    System.out.println("Выполнено: " + (log.isCompleted() ? "Да" : "Нет"));
    System.out.println();*/
      System.out.println(log.getHabit().getName() + ": " + log.getLogDate());
    }
  }

  /**
   * Deletes a log.
   *
   * @param scanner Scanner instance
   */
  private void deleteLog(Scanner scanner) {
    System.out.println("Введите ID отметки:");
    int logId = scanner.nextInt();
    logService.deleteLog(logId);
  }

  /**
   * Sends a notification.
   *
   * @param scanner Scanner instance
   */
  private void sendNotification(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    notificationService.sendNotification(userId);
  }

  /**
   * Resets a user's password.
   *
   * @param scanner Scanner instance
   */
  //ToDo Реализация сброса пароля через email (опционально).
  private void resetPassword(Scanner scanner) {
    System.out.println("Введите email:");
    String email = scanner.next();
    userService.resetPassword(email);
  }

  /**
   * Views statistics.
   *
   * @param scanner Scanner instance
   */
  private void viewStatistics(Scanner scanner) {
    User user = userService.getAuthenticatedUser();
    if (user == null) {
      System.out.println("Пожалуйста, авторизуйтесь перед просмотром статистики.");
      return;
    }

    System.out.println("Статистика выполнения привычек:");
    System.out.println("1. Подсчет текущих серий выполнения привычек (streak)");
    System.out.println("2. Процент успешного выполнения привычек за определенный период");
    System.out.println("3. Формирование отчета для пользователя по прогрессу выполнения");
    int choice = scanner.nextInt();

    switch (choice) {
      case 1:
        viewStreak(scanner);
        break;
      case 2:
        viewSuccessPercentage(scanner);
        break;
      case 3:
        viewProgressReport(scanner);
        break;
      default:
        System.out.println("Неправильный выбор");
    }
  }

  /**
   * Views the current streak for each habit.
   *
   * @param scanner Scanner instance
   */
  private void viewStreak(Scanner scanner) {
    User user = userService.getAuthenticatedUser();
    List<Habit> habits = habitService.getHabitsByUser(user);
    for (Habit habit : habits) {
      int streak = logService.getStreak(habit);
      System.out.println("Привычка: " + habit.getName() + ", Текущая серия: " + streak);
    }
  }

  /**
   * Views the success percentage for each habit.
   *
   * @param scanner Scanner instance
   */
  private void viewSuccessPercentage(Scanner scanner) {
    User user = userService.getAuthenticatedUser();
    System.out.println("Введите начало периода (yyyy-MM-dd):");
    String startDateString = scanner.next();
    System.out.println("Введите конец периода (yyyy-MM-dd):");
    String endDateString = scanner.next();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date startDate = null;
    Date endDate = null;
    try {
      startDate = sdf.parse(startDateString);
      endDate = sdf.parse(endDateString);
    } catch (ParseException e) {
      System.out.println("Ошибка: некорректная дата. " + e.getMessage());
      return;
    }

    List<Habit> habits = habitService.getHabitsByUser(user);
    for (Habit habit : habits) {
      double successPercentage = logService.getSuccessPercentage(habit, startDate, endDate);
      System.out.println(
          "Привычка: " + habit.getName() + ", Процент успешного выполнения: " + successPercentage
              + "%");
    }
  }

  /**
   * Views the progress report for each habit.
   *
   * @param scanner Scanner instance
   */
  private void viewProgressReport(Scanner scanner) {
    User user = userService.getAuthenticatedUser();
    List<Habit> habits = habitService.getHabitsByUser(user);
    for (Habit habit : habits) {
      List<Log> logs = logService.getLogsByHabit(habit);
      System.out.println("Привычка: " + habit.getName());
      for (Log log : logs) {
        System.out.println(
            "Дата: " + log.getLogDate() + ", Выполнено: " + (log.isCompleted() ? "Да" : "Нет"));
      }
    }
  }

  /**
   * Admin panel.
   *
   * @param scanner Scanner instance
   */
  private void adminPanel(Scanner scanner) {
    System.out.println("Администрирование:");
    System.out.println("1. Список пользователей");
    System.out.println("2. Список привычек");
    System.out.println("3. Блокировка пользователя");
    System.out.println("4. Удаление пользователя");
    int choice = scanner.nextInt();
    switch (choice) {
      case 1:
        viewUsers();
        break;
      case 2:
        viewHabitsForAdmin(scanner);
        break;
      case 3:
        blockUser(scanner);
        break;
      case 4:
        adminDeletedUser(scanner);
        break;
      default:
        System.out.println("Неправильный выбор");
    }
  }

  /**
   * Views users.
   */
  private void viewUsers() {
    List<User> users = userService.getUsers();
    for (User user : users) {
      System.out.println(
          "ID: " + user.getId() + ", Email: " + user.getEmail() + ", Имя: " + user.getName());
    }
  }

  /**
   * Views habits for admin.
   *
   * @param scanner Scanner instance
   */
  private void viewHabitsForAdmin(Scanner scanner) {
    List<Habit> habits = habitService.getHabits();
    for (Habit habit : habits) {
      System.out.println("ID: " + habit.getId() + ", Название: " + habit.getName() + ", Описание: "
          + habit.getDescription());
    }
  }

  private void blockUser(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    User user = userService.getUserById(userId);
    if (user != null) {
      user.setBlocked(true);
      userService.editUserProfile(user);
      System.out.println("Пользователь заблокирован");
    } else {
      System.out.println("Пользователь не найден");
    }
  }

  private void adminDeletedUser(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    User user = userService.getUserById(userId);
    if (user != null) {
      userService.deleteUser(user.getEmail());
      System.out.println("Пользователь удален");
    } else {
      System.out.println("Пользователь не найден");
    }
  }
}