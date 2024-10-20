package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

  private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";

  public NotificationService(EmailNotificationService emailNotificationService) {
    this.emailNotificationService = emailNotificationService;
  }

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
  public NotificationService(EmailNotificationService emailNotificationService,
      Connection connection) {
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
  /**
   * Creates a new notification.
   *
   * @param notificationDate the date of the notification
   * @param sent             indicates whether the notification has been sent
   * @param user             the user who will receive the notification
   * @param habit            the habit associated with the notification
   */
  public void createNotification(Date notificationDate, boolean sent, User user, Habit habit) {
    String query = "INSERT INTO postgres_schema.notifications (notification_date, sent, user_id, habit_id) VALUES (?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setDate(1, new java.sql.Date(notificationDate.getTime()));
      statement.setBoolean(2, sent);
      statement.setInt(3, user.getId());
      statement.setInt(4, habit.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка создания уведомления: " + e.getMessage());
    }
  }

  public int getNotificationCount() {
    String query = "SELECT COUNT(*) FROM postgres_schema.notifications";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения количества уведомлений: " + e.getMessage());
    }
    return 0;
  }


  public void addNotification(Notification notification) {
    String query = "INSERT INTO postgres_schema.notifications (notification_date, sent, user_id, habit_id) VALUES (?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setDate(1, new java.sql.Date(notification.getNotificationDate().getTime()));
      statement.setBoolean(2, notification.isSent());
      statement.setInt(3, notification.getUser().getId());
      statement.setInt(4, notification.getHabit().getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка добавления уведомления: " + e.getMessage());
    }
  }

  /**
   * Sends a notification to the user.
   *
   * @param notificationId the ID of the notification to send
   */
  public void sendNotification(int notificationId) {
    String query = "SELECT * FROM postgres_schema.notifications WHERE id = ?";
    try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setInt(1, notificationId);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        Notification notification = new Notification();
        notification.setId(resultSet.getInt("id"));
        notification.setNotificationDate(resultSet.getDate("notification_date"));
        notification.setSent(resultSet.getBoolean("sent"));
        notification.setUser(new User(resultSet.getInt("user_id")));
        notification.setHabit(new Habit(resultSet.getInt("habit_id")));

        // Проверяем, было ли уведомление уже отправлено
        if (!notification.isSent()) {
          emailNotificationService.sendNotification(notification.getUser().getEmail(),
              "Уведомление", "Выполняйте свою привычку!");

          // Обновляем статус отправки
          query = "UPDATE postgres_schema.notifications SET sent = ? WHERE id = ?";
          try (PreparedStatement updateStatement = conn.prepareStatement(query)) {
            updateStatement.setBoolean(1, true);
            updateStatement.setInt(2, notificationId);
            updateStatement.executeUpdate();
          } catch (SQLException e) {
            System.out.println("Ошибка обновления уведомления: " + e.getMessage());
          }
        } else {
          System.out.println("Уведомление уже было отправлено.");
        }
      } else {
        System.out.println("Уведомление с ID " + notificationId + " не найдено.");
      }
    } catch (SQLException e) {
      System.out.println("Ошибка отправки уведомления: " + e.getMessage());
    }
  }
}
