package org.example.grudapp.model;

import java.util.Date;

/**
 * Represents a notification in the system.
 */
public class Notification {

  /**
   * Unique identifier for the notification.
   */
  private final int id;
  /**
   * Date of the notification.
   */
  private final Date notificationDate;
  /**
   * Indicates whether the notification has been sent.
   */
  private boolean sent;
  /**
   * User who received the notification.
   */
  private User user;
  private Habit habit;

  /**
   * Constructs a Notification object with the given parameters.
   *
   * @param id               the unique ID for the notification
   * @param notificationDate the date of the notification
   * @param sent             whether the notification has been sent
   * @param user             the user who received the notification
   * @param habit            the habit associated with the notification
   */
  public Notification(int id, Date notificationDate, boolean sent, User user, Habit habit) {
    this.id = id;
    this.notificationDate = notificationDate;
    this.sent = sent;
    this.user = user;
    this.habit = habit;
  }

  /**
   * Returns the unique ID for the notification.
   *
   * @return the ID
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the date of the notification.
   *
   * @return the notification date
   */
  public Date getNotificationDate() {
    return notificationDate;
  }

  /**
   * Returns whether the notification has been sent.
   *
   * @return true if sent, false otherwise
   */
  public boolean isSent() {
    return sent;
  }

  public void setSent(boolean sent) {
    this.sent = sent;
  }

  /**
   * Returns the user who received the notification.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the habit associated with the notification.
   *
   * @return the habit
   */
  public Habit getHabit() {
    return habit;
  }

  public void setHabit(Habit habit) {
    this.habit = habit;
  }
}
