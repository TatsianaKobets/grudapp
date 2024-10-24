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

  private static final String URL = "jdbc:postgresql://postgres:5432/postgres?currentSchema=postgres_schema";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "password";
  private EmailNotificationService emailNotificationService;
  private Map<Integer, Notification> notifications = new HashMap<>();

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

  /**
   * Returns the total number of notifications in the database.
   *
   * @return the number of notifications
   * @throws SQLException if an error occurs while executing the database query
   */
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

  /**
   * Adds a new notification to the database.
   *
   * @param notification the notification to be added
   * @throws SQLException if an error occurs while executing the database query
   */
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

        if (!notification.isSent()) {
          emailNotificationService.sendNotification(notification.getUser().getEmail(),
              "Уведомление", "Выполняйте свою привычку!");

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
