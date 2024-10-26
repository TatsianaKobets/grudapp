package org.example.grudapp.repository;

import static org.example.grudapp.repository.DatabaseConstants.PASSWORD;
import static org.example.grudapp.repository.DatabaseConstants.URL;
import static org.example.grudapp.repository.DatabaseConstants.USERNAME;

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

public class NotificationRepository {

  EmailNotificationService emailNotificationService;

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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.CREATE_NOTIFICATION)) {
      preparedStatement.setDate(1, new java.sql.Date(notificationDate.getTime()));

      preparedStatement.setBoolean(2, sent);
      preparedStatement.setInt(3, user.getId());
      preparedStatement.setInt(4, habit.getId());

      preparedStatement.executeUpdate();

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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.GET_NOTIFICATION_COUNT)) {
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt(1);
        }
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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.ADD_NOTIFICATION)) {

      preparedStatement.setDate(1, new java.sql.Date(notification.getNotificationDate().getTime()));
      preparedStatement.setBoolean(2, notification.isSent());
      preparedStatement.setInt(3, notification.getUser().getId());
      preparedStatement.setInt(4, notification.getHabit().getId());

      preparedStatement.executeUpdate();

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

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.SEND_NOTIFICATION)) {

      preparedStatement.setInt(1, notificationId);

      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          Notification notification = new Notification();
          notification.setId(rs.getInt("id"));
          notification.setNotificationDate(rs.getDate("notification_date"));
          notification.setSent(rs.getBoolean("sent"));
          notification.setUser(new User(rs.getInt("user_id")));
          notification.setHabit(new Habit(rs.getInt("habit_id")));

          if (!notification.isSent()) {
            emailNotificationService.sendNotification(notification.getUser().getEmail(),
                "Уведомление", "Выполняйте свою привычку!");
            updateNotificationStatus(connection, notificationId);
          } else {
            System.out.println("Ошибка обновления уведомления: ");
          }
        } else {
          System.out.println("Уведомление уже было отправлено.");
        }
      }
    } catch (SQLException e) {
      System.out.println("Уведомление с ID " + notificationId + " не найдено.");
    }
  }

  private void updateNotificationStatus(Connection conn, int notificationId) throws SQLException {

    try (PreparedStatement updateStatement = conn.prepareStatement(
        QueryConstants.UPDATE_NOTIFICATION)) {

      updateStatement.setBoolean(1, true);
      updateStatement.setInt(2, notificationId);

      updateStatement.executeUpdate();

      System.out.println("Уведомление успешно отправлено.");
    } catch (SQLException e) {
      System.out.println("Ошибка отправки уведомления " + e.getMessage());
    }
  }
}
