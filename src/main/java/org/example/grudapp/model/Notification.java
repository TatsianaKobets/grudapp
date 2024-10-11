package org.example.grudapp.model;

import java.util.Date;

public class Notification {
  private int id;
  private Date notificationDate;
  private boolean sent;
  private User user;
  private Habit habit;

  public Notification(int id, Date notificationDate, boolean sent, User user, Habit habit) {
    this.id = id;
    this.notificationDate = notificationDate;
    this.sent = sent;
    this.user = user;
    this.habit = habit;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Date getNotificationDate() {
    return notificationDate;
  }

  public void setNotificationDate(Date notificationDate) {
    this.notificationDate = notificationDate;
  }

  public boolean isSent() {
    return sent;
  }

  public void setSent(boolean sent) {
    this.sent = sent;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Habit getHabit() {
    return habit;
  }

  public void setHabit(Habit habit) {
    this.habit = habit;
  }
}
