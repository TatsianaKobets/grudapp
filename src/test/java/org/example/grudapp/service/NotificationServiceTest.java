package org.example.grudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Notification;
import org.example.grudapp.model.User;
import org.example.grudapp.ui.EmailNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class NotificationServiceTest {

  private NotificationService notificationService;
  private EmailNotificationService emailNotificationService;

  @BeforeEach
  public void setUp() {
    emailNotificationService = mock(EmailNotificationService.class);
    notificationService = new NotificationService(emailNotificationService);
  }

  @Test
  public void createNotification() {
    Date notificationDate = new Date();
    User user = new User(1, "John Doe", "johndoe", "password123");
    Habit habit = new Habit(1, "Test Habit", "This is a test habit", "daily", user, new Date());
    notificationService.createNotification(notificationDate, false, user, habit);
    assertEquals(1, notificationService.getNotificationCount());
  }

  @Test
  public void sendNotification() {
    Notification notification = new Notification(1, new Date(), false,
        new User(1, "John Doe", "johndoe", "password123"),
        new Habit(1, "Test Habit", "This is a test habit", "daily",
            new User(1, "John Doe", "johndoe", "password123"), new Date()));
    notificationService.addNotification(notification);

    // Call the sendNotification method
    notificationService.sendNotification(1);

    // Verify that the notification was marked as sent
    assertTrue(notification.isSent());

    // Verify that the email notification service was called
    verify(emailNotificationService).sendNotification("John Doe", "Уведомление",
        "Выполняйте свою привычку!");
  }

  @Test
  public void sendNotification_NotFound() {
    notificationService.sendNotification(1);
    assertEquals(0, notificationService.getNotificationCount());
    verify(emailNotificationService, times(0)).sendNotification(anyString(), anyString(),
        anyString());
  }
}

