package org.example.grudapp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Notification;
import org.example.grudapp.model.User;
import org.example.grudapp.ui.EmailNotificationService;

/**
 * Provides services for managing notifications.
 */
public class NotificationService {

  /**
   * Email notification service used to send notifications.
   */
  private EmailNotificationService emailNotificationService;
  /**
   * Map of all notifications. The key is the ID of the notification, and the value is the
   * notification.
   */
  private Map<Integer, Notification> notifications = new HashMap<>();


  /**
   * Creates a new notification service with the given email notification service.
   *
   * @param emailNotificationService the email notification service to use
   */
  public NotificationService(EmailNotificationService emailNotificationService) {
    this.emailNotificationService = emailNotificationService;
  }

  /**
   * Creates a new notification.
   *
   * @param notificationDate the date of the notification
   * @param sent             indicates whether the notification has been sent
   * @param user             the user who will receive the notification
   * @param habit            the habit associated with the notification
   */
  public void createNotification(Date notificationDate, boolean sent, User user, Habit habit) {
    Notification notification = new Notification(notifications.size() + 1, notificationDate, sent,
        user, habit);
    notifications.put(notification.getId(), notification);
  }

  public int getNotificationCount() {
    return notifications.size();
  }

  public void addNotification(Notification notification) {
    notifications.put(notification.getId(), notification);
  }

  /**
   * Sends a notification to the user.
   *
   * @param notificationId the ID of the notification to send
   */
  public void sendNotification(int notificationId) {
    for (Notification notification : notifications.values()) {
      if (notification.getId() == notificationId) {
        notification.setSent(true);
        // Отправка уведомления через email или push-уведомления
        emailNotificationService.sendNotification(notification.getUser().getEmail(), "Уведомление",
            "Выполняйте свою привычку!");
        return;
      }
    }
  }
}
