package org.example.grudapp.ui;

import java.sql.SQLException;
import java.util.Scanner;
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
  public void run() throws SQLException {
    Scanner scanner = new Scanner(System.in);
    User currentUser = null;
    System.out.println("Вы не авторизованы. Выберите действие:");
    startPanel(scanner);
    System.out.print("Вы хотите выйти? (да/нет): ");
    String input = scanner.next();
    if (input.equalsIgnoreCase("да")) {
      System.exit(0);
    } else {
      startPanel(scanner);
    }
  }

  private void startPanel(Scanner scanner) throws SQLException {
    System.out.println("1. Регистрация");
    System.out.println("2. Авторизация");
    System.out.println("3. Выход");
    int choice = scanner.nextInt();
    switch (choice) {
      case 1 -> AuthenticateAndRegisterModule.registerUser(scanner);
      case 2 -> AuthenticateAndRegisterModule.authenticateUser(scanner);
      case 3 -> System.exit(0);
      default -> System.out.println("Неправильный выбор");
    }
  }

  void userPanel(Scanner scanner, User currentUser) throws SQLException {
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
      case 1 -> AuthenticateAndRegisterModule.editUserProfile(scanner, currentUser);
      case 2 -> AuthenticateAndRegisterModule.changeUserEmail(scanner, currentUser);
      case 3 -> AuthenticateAndRegisterModule.deleteUser(scanner, currentUser);
      case 4 -> HabitManagementModule.createHabit(scanner, currentUser);
      case 5 -> HabitManagementModule.viewHabits(scanner, currentUser);
      case 6 -> HabitManagementModule.deleteHabit(scanner, currentUser);
      case 7 -> LogManagementModule.createLog(scanner, currentUser);
      case 8 -> LogManagementModule.viewLogs(currentUser);
      case 9 -> LogManagementModule.deleteLog(scanner);
      case 10 -> System.exit(0);
      default -> System.out.println("Неправильный выбор");
    }
  }
}