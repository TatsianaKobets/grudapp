package org.example.grudapp.model;

import java.util.Date;

/**
 * Represents a log entry in the system.
 */
public class Log {

  /**
   * Unique identifier for the log entry.
   */
  private final int id;
  /**
   * Date of the log entry.
   */
  private Date logDate;
  /**
   * Indicates whether the log entry is completed.
   */
  private boolean completed;
  /**
   * Habit associated with the log entry.
   */
  private Habit habit;
  /**
   * User who created the log entry.
   */
  private User user;

  /**
   * Constructs a Log object with the given parameters.
   *
   * @param id        the unique ID for the log entry
   * @param logDate   the date of the log entry
   * @param completed whether the log entry is completed
   * @param habit     the habit associated with the log entry
   * @param user      the user who created the log entry
   */
  public Log(int id, Date logDate, boolean completed, Habit habit, User user) {
    this.id = id;
    this.logDate = logDate;
    this.completed = completed;
    this.habit = habit;
    this.user = user;
  }

  /**
   * Returns the unique ID for the log entry.
   *
   * @return the ID
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the date of the log entry.
   *
   * @return the log date
   */
  public Date getLogDate() {
    return logDate;
  }

  public void setLogDate(Date logDate) {
    this.logDate = logDate;
  }

  /**
   * Returns whether the log entry is completed.
   *
   * @return true if completed, false otherwise
   */
  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  /**
   * Returns the habit associated with the log entry.
   *
   * @return the habit
   */
  public Habit getHabit() {
    return habit;
  }

  /**
   * Returns the user who created the log entry.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
