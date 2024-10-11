package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Notification;
import org.example.grudapp.model.User;
import org.example.grudapp.ui.EmailNotificationService;

public class NotificationService {
  private EmailNotificationService emailNotificationService;
  private List<Notification> notifications = new ArrayList<>();

  public void createNotification(Date notificationDate, boolean sent, User user, Habit habit) {
    Notification notification = new Notification(notifications.size() + 1, notificationDate, sent, user, habit);
    notifications.add(notification);
  }
  public NotificationService(EmailNotificationService emailNotificationService) {
    this.emailNotificationService = emailNotificationService;
  }
  public void sendNotification(int notificationId) {
    for (Notification notification : notifications) {
      if (notification.getId() == notificationId) {
        notification.setSent(true);
        // Отправка уведомления через email или push-уведомления
        emailNotificationService.sendNotification(notification.getUser().getEmail(), "Уведомление", "Выполняйте свою привычку!");
        return;
      }
    }
  }
}
