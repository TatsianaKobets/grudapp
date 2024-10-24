package org.example.grudapp;

import java.sql.SQLException;
import liquibase.exception.LiquibaseException;
import org.example.grudapp.config.DatabaseManager;
import org.example.grudapp.repository.HabitRepository;
import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
import org.example.grudapp.service.UserService;
import org.example.grudapp.ui.ConsoleInterface;

public class MainClass {

  public static void main(String[] args) {
    try {
      DatabaseManager.migrate();
      HabitRepository habitRepository = new HabitRepository();
      UserService userService = new UserService();
      HabitService habitService = new HabitService(habitRepository);
      LogService logService = new LogService();
      ConsoleInterface consoleInterface = new ConsoleInterface(userService, habitService,
          logService);
      consoleInterface.run();

    } catch (SQLException | LiquibaseException e) {
      System.out.println("Error: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
