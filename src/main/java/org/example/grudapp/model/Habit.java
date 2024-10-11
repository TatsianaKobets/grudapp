package org.example.grudapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Habit {

  private int id;
  private String name;
  private String description;
  private String frequency;
  private List<Log> logs;
  private User user;
  private Date creationDate;

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

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public List<Log> getLogs() {
    return logs;
  }

  public void setLogs(List<Log> logs) {
    this.logs = logs;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public boolean isCompleted() {
    Date today = new Date();
    for (Log log : logs) {
      if (frequency.equals("daily") && log.getLogDate().compareTo(today) >= 0) {
        return true;
      } else if (frequency.equals("weekly") && log.getLogDate().compareTo(today) >= -7) {
        return true;
      }
    }
    return false;
  }
}
