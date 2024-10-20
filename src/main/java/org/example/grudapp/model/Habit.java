package org.example.grudapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a habit in the system.
 */
public class Habit {

  /**
   * Unique identifier for the habit.
   */
  private int id;
  /**
   * Name of the habit.
   */
  private String name;
  /**
   * Description of the habit.
   */
  private String description;
  /**
   * Frequency of the habit (e.g. daily, weekly).
   */
  private String frequency;
  /**
   * List of logs for the habit.
   */
  private List<Log> logs;
  /**
   * User who owns the habit.
   */
  private User user;
  /**
   * Date when the habit was created.
   */
  private Date creationDate;

  /**
   * Constructs a Habit object with the given parameters.
   *
   * @param id           the unique ID for the habit
   * @param name         the name of the habit
   * @param description  the description of the habit
   * @param frequency    the frequency of the habit
   * @param user         the user who owns the habit
   * @param creationDate the date when the habit was created
   */
  public Habit(int id, String name, String description, String frequency, User user,
      Date creationDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.frequency = frequency;
    this.logs = new ArrayList<>();
    this.user = user;
    this.creationDate = new Date();
  }
  public Habit(int id, String name, String description, Date creationDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.logs = new ArrayList<>();
    this.creationDate = new Date();
  }

  public Habit() {
  }

  public Habit(int habitId) {
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Returns the unique ID for the habit.
   *
   * @return the ID
   */
  public int getId() {
    return id;
  }

  /**
   * Returns the name of the habit.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the description of the habit.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }
  /**
   * Returns the frequency of the habit.
   *
   * @return the frequency
   */
  public String getFrequency() {
    return frequency;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  /**
   * Returns the list of logs for the habit.
   *
   * @return the list of logs
   */
  public List<Log> getLogs() {
    return logs;
  }

  /**
   * Returns the user who owns the habit.
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
   * Returns the date when the habit was created.
   *
   * @return the creation date
   */
  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * Checks if the habit is completed based on the frequency and logs.
   *
   * @return true if the habit is completed, false otherwise
   */
  public boolean isCompleted() {
    Date today = new Date();
    int daysSinceLastLog = 0;
    for (Log log : logs) {
      if (frequency.equals("daily")) {
        daysSinceLastLog = (int) ((today.getTime() - log.getLogDate().getTime()) / (1000 * 60 * 60
            * 24));
        if (daysSinceLastLog <= 1) {
          return true;
        }
      } else if (frequency.equals("weekly")) {
        daysSinceLastLog = (int) ((today.getTime() - log.getLogDate().getTime()) / (1000 * 60 * 60
            * 24 * 7));
        if (daysSinceLastLog <= 1) {
          return true;
        }
      }
    }
    return false;
  }
}
