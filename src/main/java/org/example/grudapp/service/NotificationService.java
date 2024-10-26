package org.example.grudapp.service;

import java.util.Date;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Notification;
import org.example.grudapp.model.User;
import org.example.grudapp.repository.NotificationRepository;
import org.example.grudapp.repository.UserRepository;

/**
 * Provides services for managing notifications.
 */
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;

  public NotificationService(NotificationRepository notificationRepository,
      UserRepository userRepository) {
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
  }

  public void createNotification(Date notificationDate, boolean sent, int userId, int habitId) {
    User user = userRepository.getUserById(userId);
    Notification notification = new Notification(notificationDate, sent, user, new Habit(habitId));
    notificationRepository.addNotification(notification);
  }

  public int getNotificationCount() {
    return notificationRepository.getNotificationCount();
  }

  public void addNotification(Notification notification) {
    notificationRepository.addNotification(notification);
  }

  public void sendNotification(int notificationId) {
    notificationRepository.sendNotification(notificationId);
  }

  //ToDo
  public void sendNotificationsToUser(int userId) {
    // Получить уведомления для пользователя
    // Реализовать логику отправки уведомлений пользователю
  }
}
