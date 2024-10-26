package org.example.grudapp.ui;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;
import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
import org.example.grudapp.service.UserService;

public class HabitManagementModule {

  private static UserService userService;
  private static HabitService habitService;
  private LogService logService;

  static void createHabit(Scanner scanner, User currentUser) {
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
    habitService.createHabit(habitName, habitDescription, frequency);
  }

  static void viewHabits(Scanner scanner, User currentUser) {
    User user = userService.getUserByEmail(scanner.nextLine());
    if (user == null) {
      System.out.println("Вы не авторизованы. Пожалуйста, авторизуйтесь.");
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

  static void deleteHabit(Scanner scanner, User currentUser) throws SQLException {
    System.out.println("Введите ID привычки:");
    int habitId = scanner.nextInt();
    habitService.deleteHabit(habitId);
  }
}
