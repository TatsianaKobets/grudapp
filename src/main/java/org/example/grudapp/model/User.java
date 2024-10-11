package org.example.grudapp.model;

import java.util.ArrayList;
import java.util.List;

public class User {

  private int id;
  private String email;
  private String password;
  private String name;
  private List<Habit> habits;
  private List<Log> logs;
  private boolean blocked;

  public boolean isBlocked() {
    return blocked;
  }public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  public User(int id, String email, String password, String name) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.habits = new ArrayList<>();
    this.logs = new ArrayList<>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Habit> getHabits() {
    return habits;
  }

  public void setHabits(List<Habit> habits) {
    this.habits = habits;
  }

  public List<Log> getLogs() {
    return logs;
  }

  public void setLogs(List<Log> logs) {
    this.logs = logs;
  }
}
