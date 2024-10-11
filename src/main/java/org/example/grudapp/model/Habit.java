package org.example.grudapp.model;

import java.util.ArrayList;
import java.util.List;

public class Habit {
  private int id;
  private String name;
  private String description;
  private String frequency;
  private List<Log> logs;
  private User user;

  public Habit(int id, String name, String description, String frequency, User user) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.frequency = frequency;
    this.logs = new ArrayList<>();
    this.user = user;
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
}
