package org.example.grudapp.ui;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;
import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
import org.example.grudapp.service.UserService;

public class AdminModule {

  private static UserService userService;
  private static HabitService habitService;
  private static LogService logService;

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
    Map<Integer, User> admins = userService.getUsersByRole("ADMIN");
    System.out.println("Все администраторы:");
    for (User admin : admins.values()) {
      System.out.println(
          "ID: " + admin.getId() + ", Email: " + admin.getEmail() + ", Name: " + admin.getName());
    }
  }

  private void viewAllUsers(Scanner scanner) {
    Map<Integer, User> users = userService.getUsersByRole("USER");
    System.out.println("Все пользователи:");
    for (User user : users.values()) {
      System.out.println(
          "ID: " + user.getId() + ", Email: " + user.getEmail() + ", Name: " + user.getName()
              + ", Role: " + user.getRole());
    }
  }

  private void viewUsers() {
    List<User> users = userService.getUsers();
    for (User user : users) {
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

  private void viewAllHabits(Scanner scanner) {
    Map<Integer, Habit> habits = habitService.getHabits();
    for (Habit habit : habits.values()) {
      System.out.println("ID: " + habit.getId() + ", Название: " + habit.getName() + ", Описание: "
          + habit.getDescription());
    }
  }

}
