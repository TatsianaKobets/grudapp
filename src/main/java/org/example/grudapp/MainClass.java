package org.example.grudapp;

import org.example.grudapp.service.HabitService;
import org.example.grudapp.service.LogService;
import org.example.grudapp.service.NotificationService;
import org.example.grudapp.service.UserService;
import org.example.grudapp.ui.ConsoleInterface;
import org.example.grudapp.ui.EmailNotificationService;

public class MainClass {

  public static void main(String[] args) {
    UserService userService = new UserService();
    HabitService habitService = new HabitService();
    LogService logService = new LogService();
    NotificationService notificationService = new NotificationService(
        new EmailNotificationService("smtp.gmail.com", "587", "ваш_емейл@gmail.com", "ваш_пароль"));
    ConsoleInterface consoleInterface = new ConsoleInterface(
        userService, habitService, logService, notificationService);
    consoleInterface.run();
  }
}
