package org.example.grudapp.model;

public class Admin {
  private int id;
  private User user;

  public Admin(int id, User user) {
    this.id = id;
    this.user = user;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
