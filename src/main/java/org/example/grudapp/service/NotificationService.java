package org.example.grudapp.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.example.grudapp.dbconnect.DatabaseConnector;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Notification;
import org.example.grudapp.model.User;
import org.example.grudapp.ui.EmailNotificationService;

/**
 * Provides services for managing notifications.
 */
public class NotificationService {

  private Connection connection;

  public NotificationService() {
    try {
      connection = DatabaseConnector.getConnection();
    } catch (SQLException e) {
      System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
    }
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
    this.connection = connection;
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
    String query = "INSERT INTO notifications (notification_date, sent, user_id, habit_id) VALUES (?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setDate(1, new java.sql.Date(notificationDate.getTime()));
      statement.setBoolean(2, sent);
      statement.setInt(3, user.getId());
      statement.setInt(4, habit.getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка создания уведомления: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
  }

  public int getNotificationCount() {
    String query = "SELECT COUNT(*) FROM notifications";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getInt(1);
      }
    } catch (SQLException e) {
      System.out.println("Ошибка получения количества уведомлений: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
    return 0;
  }

  public void addNotification(Notification notification) {
    String query = "INSERT INTO notifications (id, notification_date, sent, user_id, habit_id) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, notification.getId());
      statement.setDate(2, new java.sql.Date(notification.getNotificationDate().getTime()));
      statement.setBoolean(3, notification.isSent());
      statement.setInt(4, notification.getUser().getId());
      statement.setInt(5, notification.getHabit().getId());
      statement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка добавления уведомления: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
  }

  /**
   * Sends a notification to the user.
   *
   * @param notificationId the ID of the notification to send
   */
  public void sendNotification(int notificationId) {
    String query = "SELECT * FROM notifications WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setInt(1, notificationId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        Notification notification = new Notification();
        notification.setId(resultSet.getInt("id"));
        notification.setNotificationDate(resultSet.getDate("notification_date"));
        notification.setSent(resultSet.getBoolean("sent"));
        notification.setUser(new User(resultSet.getInt("user_id")));
        notification.setHabit(new Habit(resultSet.getInt("habit_id")));
        notification.setSent(true);
        emailNotificationService.sendNotification(notification.getUser().getEmail(), "Уведомление",
            "Выполняйте свою привычку!");
        query = "UPDATE notifications SET sent = ? WHERE id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
          updateStatement.setBoolean(1, true);
          updateStatement.setInt(2, notificationId);
          updateStatement.executeUpdate();
        } catch (SQLException e) {
          System.out.println("Ошибка обновления уведомления: " + e.getMessage());
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка отправки уведомления: " + e.getMessage());
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (SQLException e) {
          System.out.println("Ошибка закрытия соединения с базой данных: " + e.getMessage());
        }
      }
    }
  }
}
