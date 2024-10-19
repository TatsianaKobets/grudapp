package org.example.grudapp;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
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

  public static void main(String[] args) {
    Properties properties = new Properties();
    try (InputStream input = MainClass.class.getClassLoader()
        .getResourceAsStream("config.properties")) {
      if (input == null) {
        System.out.println("Sorry, unable to find config.properties");
        return;
      }

      properties.load(input);
      String url = properties.getProperty("db.url");
      String username = properties.getProperty("db.username");
      String password = properties.getProperty("db.password");

      Connection connection = DriverManager.getConnection(url, username, password);
      Database database = DatabaseFactory.getInstance()
          .findCorrectDatabaseImplementation(new JdbcConnection(connection));
      Liquibase liquibase = new Liquibase("db/changelog/changelog.xml",
          new ClassLoaderResourceAccessor(), database);
      liquibase.update();
      System.out.println("Migration is completed successfully");

      UserService userService = new UserService();
      HabitService habitService = new HabitService();
      LogService logService = new LogService();
      AdminService adminService = new AdminService();
      ConsoleInterface consoleInterface = new ConsoleInterface(userService, habitService,
          logService, adminService);
      consoleInterface.run();

    } catch (SQLException | LiquibaseException | IOException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}