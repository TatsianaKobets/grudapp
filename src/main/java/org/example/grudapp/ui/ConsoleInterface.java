package org.example.grudapp.ui;

import java.sql.SQLException;
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
import org.example.grudapp.model.User;
import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
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

  /**
   * Constructor for ConsoleInterface.
   *
   * @param userService  UserService instance
   * @param habitService HabitService instance
   * @param logService   LogService instance
   */

  public ConsoleInterface(UserService userService, HabitService habitService,
      LogService logService) {
    this.userService = userService;
    this.habitService = habitService;
    this.logService = logService;
  }

  /**
   * Runs the console interface.
   */
  public void run() {
    Scanner scanner = new Scanner(System.in);
    boolean running = true;
    while (running) {
      User currentUser = userService.getAuthenticatedUser(); // Получаем текущего аутентифицированного пользователя
      if (currentUser == null) {
        System.out.println("Вы не авторизованы. Выберите действие:");
        startPanel(scanner);
      } else {
        try {
          userPanel(scanner); // Используем один метод, чтобы выводить меню пользователя
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      }
      System.out.print("Do you want to exit? (yes/no): ");
      String input = scanner.next();
      if (input.equalsIgnoreCase("yes")) {
        running = false;
      }
    }
  }

  private void startPanel(Scanner scanner) {
    System.out.println("1. Регистрация");
    System.out.println("2. Авторизация");
    System.out.println("3. Выход");
    int choice = scanner.nextInt();
    switch (choice) {
      case 1 -> registerUser(scanner);
      case 2 -> authenticateUser(scanner);
      case 3 -> System.exit(0);
      default -> System.out.println("Неправильный выбор");
    }
  }

  private void userPanel(Scanner scanner) throws SQLException {
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
      case 1 -> editUserProfile(scanner);
      case 2 -> changeUserEmail(scanner);
      case 3 -> deleteUser(scanner);
      case 4 -> createHabit(scanner, userService.getAuthenticatedUser());
      case 5 -> viewHabits(scanner, userService.getAuthenticatedUser());
      case 6 -> deleteHabit(scanner);
      case 7 -> createLog(scanner);
      case 8 -> viewLogs();
      case 9 -> deleteLog(scanner);
      case 10 -> System.exit(0);
      default -> System.out.println("Неправильный выбор");
    }
  }

  private void adminPanel(Scanner scanner) {
    System.out.println(
        "Вы авторизованы как администратор" + userService.getUserByEmail(scanner.nextLine())
            .getName()
            + "\n Выберите действие:");
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
      case 1 -> assignAdminRole(scanner);
      case 2 -> assignUserRole(scanner);
      case 3 -> blockUser(scanner);
      case 4 -> adminDeletedUser(scanner);
      case 5 -> viewAllAdmins(scanner);
      case 6 -> viewAllUsers(scanner);
      case 7 -> viewAllHabits(scanner);
      case 8 -> System.exit(0);
      default -> System.out.println("Неправильный выбор");
    }
  }

  private void assignAdminRole(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    User user = userService.getUserById(userId);
    if (user != null) {
      userService.assignAdminRole(user);
      System.out.println("Роль ADMIN назначена пользователю");
    } else {
      System.out.println("Пользователь не найден");
    }
  }

  private void assignUserRole(Scanner scanner) {
    System.out.println("Введите ID пользователя:");
    int userId = scanner.nextInt();
    User user = userService.getUserById(userId);
    if (user != null) {
      userService.assignUserRole(user);
      System.out.println("Роль USER назначена пользователю");
    } else {
      System.out.println("Пользователь не найден");
    }
  }

  private void viewAllAdmins(Scanner scanner) {
    Set<User> admins = userService.getAdmins();
    System.out.println("Все администраторы:");
    for (User admin : admins) {
      System.out.println(
          "ID: " + admin.getId() + ", Email: " + admin.getEmail() + ", Name: " + admin.getName());
    }
  }

  private void viewAllUsers(Scanner scanner) {
    Map<Integer, User> users = userService.getUsers();
    System.out.println("Все пользователи:");
    for (User user : users.values()) {
      System.out.println(
          "ID: " + user.getId() + ", Email: " + user.getEmail() + ", Name: " + user.getName()
              + ", Role: " + user.getRole());
    }
  }

  private void viewAllHabits(Scanner scanner) {
    Map<Integer, Habit> habits = habitService.getHabits();
    for (Habit habit : habits.values()) {
      System.out.println("ID: " + habit.getId() + ", Название: " + habit.getName() + ", Описание: "
          + habit.getDescription());
    }
  }

  private void registerUser(Scanner scanner) {
    // проверка ввода и вызов сервиса регистрации
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

  private void deleteUser(Scanner scanner) {
    System.out.println("Введите email пользователя:");
    String email = scanner.next();
    userService.deleteUser(email);
    System.out.println("Пользователь с email " + email + " успешно удален");
  }

  private void createHabit(Scanner scanner, User user) {
    // Убедимся, что пользователь действительно авторизован
    if (user == null) {
      System.out.println("Ошибка: пользователь не авторизован.");
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
      case 1 -> frequency = "DAILY";
      case 2 -> frequency = "WEEKLY";
      default -> {
        System.out.println(
            "Неправильный выбор. Частота выполнения будет установлена как ежедневно.");
        frequency = "DAILY";
      }
    }

    // Создание привычки с переданными параметрами
    habitService.createHabit(habitName, habitDescription, frequency, user);
  }

  private void viewHabits(Scanner scanner, User user) {
    // Убедимся, что пользователь действительно авторизован
    if (user == null) {
      System.out.println("Ошибка: пользователь не авторизован.");
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
      case 1 -> {
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
      }
      case 2 -> {
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
      }
      default -> System.out.println("Неправильный выбор. Просмотр привычек без фильтрации.");
    }

    System.out.println("Ваши привычки:");
    for (Habit habit : habits) {
      System.out.println(
          habit.getId() + ". " + habit.getName() + ": " + habit.getDescription());
    }
  }

  private void deleteHabit(Scanner scanner) throws SQLException {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();
    habitService.deleteHabit(habitId);
  }

  private void createLog(Scanner scanner) {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();

    // Считываем оставшийся символ новой строки
    scanner.nextLine();

    // Запрашиваем у пользователя дату выполнения
    System.out.println("Введите дату выполнения (например yyyy-MM-dd):");
    String logDateString = scanner.nextLine();

    // Обрабатываем дату
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

    // Получаем аутентифицированного пользователя
    User user = userService.getAuthenticatedUser();
    // Получаем привычки пользователя
    List<Habit> habits = habitService.getHabitsByUser(user);

    // Проверяем, содержит ли пользователь эту привычку
    Habit habit = habits.stream()
        .filter(h -> h.getId() == habitId)
        .findFirst()
        .orElse(null);

    if (habit == null) {
      System.out.println("Некорректный ID привычки. Пожалуйста, введите корректный ID.");
      return;
    }

    // Создание лога
    logService.createLog(logDate, completed, habit, user);
    System.out.println("Лог успешно добавлен.");
  }

  private void viewLogs() {
    User currentUser = userService.getAuthenticatedUser();
    if (currentUser == null) {
      System.out.println("Ошибка: пользователь не авторизован.");
      return;
    }

    List<Log> logs = logService.getLogsByUser(
        currentUser); // Получаем логи для текущего пользователя
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

  private void deleteLog(Scanner scanner) {
    System.out.println("Введите ID отметки:");
    int logId = scanner.nextInt();
    logService.deleteLog(logId);
  }

  private void resetPassword(Scanner scanner) {
    System.out.println("Введите email:");
    String email = scanner.next();
    userService.resetPassword(email);
  }

  private void viewStatistics(Scanner scanner) {
    User user = userService.getUserByEmail(scanner.nextLine());
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
      case 1 -> viewStreak(scanner);
      case 2 -> viewSuccessPercentage(scanner);
      case 3 -> viewProgressReport(scanner);
      default -> System.out.println("Неправильный выбор");
    }
  }

  private void viewStreak(Scanner scanner) {
    User user = userService.getUserByEmail(scanner.nextLine());
    List<Habit> habits = habitService.getHabitsByUser(user);
    for (Habit habit : habits) {
      int streak = logService.getStreak(habit);
      System.out.println("Привычка: " + habit.getName() + ", Текущая серия: " + streak);
    }
  }

  private void viewSuccessPercentage(Scanner scanner) {
    User user = userService.getUserByEmail(scanner.nextLine());
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
              + successPercentage + "%");
    }
  }

  private void viewProgressReport(Scanner scanner) {
    User user = userService.getUserByEmail(scanner.nextLine());
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