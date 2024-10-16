package org.example.grudapp.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Log;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;
import org.example.grudapp.service.AdminService;
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

  private AdminService adminService;

  /**
   * Constructor for ConsoleInterface.
   *
   * @param userService         UserService instance
   * @param habitService        HabitService instance
   * @param logService          LogService instance
   * @param notificationService NotificationService instance
   * @param adminService        AdminService instance
   */
  public ConsoleInterface(UserService userService, HabitService habitService, LogService logService,
      NotificationService notificationService, AdminService adminService) {
    this.userService = userService;
    this.habitService = habitService;
    this.logService = logService;
    this.notificationService = notificationService;
    this.adminService = adminService;
  }

  /**
   * Runs the console interface.
   */
  public void run() {
    Scanner scanner = new Scanner(System.in);
    // Если пользователь не авторизован, показываем только вход и регистрацию
    while (true) {
      if (userService.getAuthenticatedUser() == null) {
        System.out.println("Вы не авторизованы. Выберите действие:");
        startPanel(scanner);
      } else if (userService.getAuthenticatedUser().getRole() == Role.USER) {
        // Если пользователь авторизован, но не админ, показываем пункты меню для пользователя
        System.out.println("Вы авторизованы как " + userService.getAuthenticatedUser().getName()
            + " . Выберите действие:");
        userPanel(scanner);
      } else if (userService.getAuthenticatedUser().getRole() == Role.ADMIN) {
        // Если пользователь админ, показываем все пункты меню
        System.out.println(
            "Вы авторизованы как администратор" + userService.getAuthenticatedUser().getName()
                + "\n У Вас рассширенные права. Выберите действие:");
        System.out.println("1. Продолжить как администратор");
        System.out.println("2. Продолжить как пользователь");
        System.out.println("3. Выход");
        int choice = scanner.nextInt();
        switch (choice) {
          case 1:
            adminPanel(scanner);
            break;
          case 2:
            userPanel(scanner);
            break;
          case 3:
            System.exit(0);
            break;
          default:
            System.out.println("Неправильный выбор");
        }

      }
    }
  }

  private void startPanel(Scanner scanner) {
    System.out.println("1. Регистрация");
    System.out.println("2. Авторизация");
    System.out.println("3. Выход");
    int choice = scanner.nextInt();
    switch (choice) {
      case 1:
        registerUser(scanner);
        break;
      case 2:
        authenticateUser(scanner);
        break;
      case 3:
        System.exit(0);
        break;
      default:
        System.out.println("Неправильный выбор");
    }
  }

  private void userPanel(Scanner scanner) {
    System.out.println("1. Изменить данные пользователя");
    System.out.println("2. Изменить email пользователя");
    System.out.println("3. Удалить пользователя");
    System.out.println("4. Создать привычку");
    System.out.println("5. Просмотреть привычки");
    System.out.println("6. Удалить привычку");
    System.out.println("7. Создать отметку о выполнении");
    System.out.println("8. Просмотреть отметки о выполнении");
    System.out.println("9. Удалить отметку о выполнении");
    System.out.println("10. Выход");
    int choice = scanner.nextInt();
    switch (choice) {
      case 1:
        editUserProfile(scanner);
        break;
      case 2:
        changeUserEmail(scanner);
        break;
      case 3:
        deleteUser(scanner);
        break;
      case 4:
        createHabit(scanner);
        break;
      case 5:
        viewHabits(scanner);
        break;
      case 6:
        deleteHabit(scanner);
        break;
      case 7:
        createLog(scanner);
        break;
      case 8:
        viewLogs();
        break;
      case 9:
        deleteLog(scanner);
        break;
      case 10:
        System.exit(0);
        break;
      default:
        System.out.println("Неправильный выбор");
    }
  }

  /**
   * Admin panel.
   *
   * @param scanner Scanner instance
   */
  private void adminPanel(Scanner scanner) {
    System.out.println(
        "Вы авторизованы как администратор" + userService.getAuthenticatedUser().getName()
            + "\n Выберите действие:");
    System.out.println("1. Назначить роль администратора для пользователя");
    System.out.println("2. Назначить роль пользователя для администратора");
    System.out.println("3. Заблокировать пользователя");
    System.out.println("4. Удалить пользователя из системы администратором");
    System.out.println("5. Просмотреть всех администраторов");
    System.out.println("6. Просмотреть всех пользователей");
    System.out.println("7. Просмотреть все привычки");
    System.out.println("8. Выход");
    int choice = scanner.nextInt();
    switch (choice) {
      case 1:
        assignAdminRole(scanner);
        break;
      case 2:
        assignUserRole(scanner);
       // viewHabitsForAdmin(scanner);
        break;
      case 3:
        blockUser(scanner);
        break;
      case 4:
        adminDeletedUser(scanner);
        break;
      case 5:
        viewAllAdmins(scanner);
        break;
      case 6:
        viewAllUsers(scanner);
        break;
      case 7:
        viewAllHabits(scanner);
        break;
      case 8:
        System.exit(0);
        break;
      default:
        System.out.println("Неправильный выбор");
    }
  }
  /**
   * Assigns the admin role to a user.
   *
   * @param scanner Scanner instance
   */
  private void assignAdminRole(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    User user = userService.getUserById(userId);
    if (user != null) {
      adminService.assignAdminRole(user);
      System.out.println("Роль ADMIN назначена пользователю");
    } else {
      System.out.println("Пользователь не найден");
    }
  }
  /**
   * Assigns the user role to a user.
   *
   * @param scanner Scanner instance
   */
  private void assignUserRole(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    User user = userService.getUserById(userId);
    if (user != null) {
      adminService.assignUserRole(user);
      System.out.println("Роль USER, назначенная пользователю");
    } else {
      System.out.println("Пользователь не найден");
    }
  }
  /**
   * Views all admins.
   */
  private void viewAllAdmins(Scanner scanner) {
    Set<User> admins = adminService.getAdmins();
    System.out.println("Все администраторы:");
    for (User admin : admins) {
      System.out.println("ID: " + admin.getId() + ", Email: " + admin.getEmail() + ", Name: " + admin.getName());
    }
  }

  /**
   * Views all users.
   */
  private void viewAllUsers(Scanner scanner) {
    Set<User> users = adminService.getUsers();
    System.out.println("Все пользователи:");
    for (User user : users) {
      System.out.println("ID: " + user.getId() + ", Email: " + user.getEmail() + ", Name: " + user.getName() + ", Role: " + user.getRole());
    }
  }

  /**
   * Views all habits.
   */
  private void viewAllHabits(Scanner scanner) {
    Set<Habit> habits = adminService.getHabits();
    for (Habit habit : habits) {
      System.out.println("ID: " + habit.getId() + ", Название: " + habit.getName() + ", Описание: " + habit.getDescription());
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
    System.out.println("Регистрация прошла успешно");
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

    habitService.createHabit(habitName, habitDescription, frequency, user);
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
      System.out.println(
          habit.getId() + ". " + habit.getName() + ": " + habit.getDescription());
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
    for (Habit h : habitService.getHabits().values()) {
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
    Map<Integer, Log> logs = logService.getLogs();
    for (Log log : logs.values()) {
      System.out.println("Дата выполнения: " + log.getLogDate());
      System.out.println("Название привычки: " + log.getHabit().getName());
      System.out.println("Выполнено: " + (log.isCompleted() ? "Да" : "Нет"));
      System.out.println("Создатель: " + log.getUser().getName());
      System.out.println();
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
          "Привычка: " + habit.getName() + ", Процент успешного выполнения: "
              + successPercentage
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
   * Views users.
   */
  private void viewUsers() {
    Map<Integer, User> users = userService.getUsers();
    for (User user : users.values()) {
      System.out.println(
          "ID: " + user.getId() + ", Email: " + user.getEmail() + ", Имя: " + user.getName());
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