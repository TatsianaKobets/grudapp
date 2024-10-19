package org.example.grudapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.grudapp.service.AdminService;
import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
import org.example.grudapp.service.UserService;
import org.example.grudapp.ui.ConsoleInterface;

public class MainClass {

  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  public static void main(String[] args) {
    try {
      Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
          new ClassLoaderResourceAccessor(), database);
      liquibase.update();
      System.out.println("Migration is completed successfully");
    } catch (SQLException | LiquibaseException e) {
      System.out.println("SQL xeption in migration: " + e.getMessage());
    }
    UserService userService = new UserService();
    HabitService habitService = new HabitService();
    LogService logService = new LogService();
    AdminService adminService = new AdminService();
    ConsoleInterface consoleInterface = new ConsoleInterface(
        userService, habitService, logService, adminService);
    consoleInterface.run();
  }
}