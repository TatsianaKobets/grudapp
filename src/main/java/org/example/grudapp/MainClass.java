package org.example.grudapp;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.example.grudapp.service.AdminService;
import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
import org.example.grudapp.service.NotificationService;
import org.example.grudapp.service.UserService;
import org.example.grudapp.ui.ConsoleInterface;
import org.example.grudapp.ui.EmailNotificationService;

public class MainClass {
  private static Connection loadConnection() throws ClassNotFoundException, SQLException {
    var config = new Properties();
    try (InputStream in = MainClass.class.getClassLoader()
        .getResourceAsStream("app.properties")) {
      config.load(in);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
    String url = loadSysEnvIfNullThenConfig("JDBC_URL", "url", config);
    String username = loadSysEnvIfNullThenConfig("JDBC_USERNAME", "username", config);
    String password = loadSysEnvIfNullThenConfig("JDBC_PASSWORD", "password", config);
    String driver = loadSysEnvIfNullThenConfig("JDBC_DRIVER", "driver-class-name", config);
    System.out.println("url=" + url);
    Class.forName(driver);
    return DriverManager.getConnection(
        url, username, password
    );
  }
  private static String loadSysEnvIfNullThenConfig(String sysEnv, String key, Properties config) {
    String value = System.getenv(sysEnv);
    if (value == null) {
      value = config.getProperty(key);
    }
    return value;
  }

  public static void main(String[] args) {
    UserService userService = new UserService();
    HabitService habitService = new HabitService();
    LogService logService = new LogService();
    AdminService adminService = new AdminService();
  /*  NotificationService notificationService = new NotificationService(
        new EmailNotificationService("smtp.gmail.com", "587", "ваш_емейл@gmail.com", "ваш_пароль"));*/
    ConsoleInterface consoleInterface = new ConsoleInterface(
        userService, habitService, logService, adminService);
    consoleInterface.run();
  }
}
