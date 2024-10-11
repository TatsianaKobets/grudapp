package org.example.grudapp.model;

/**
 * The Admin class represents the system administrator.
 */
public class Admin {

  /**
   * Unique identifier for the administrator.
   */
  private int id;
  /**
   * User object associated with the administrator.
   */
  private User user;

  /**
   * Constructs an Admin object with the given ID and user.
   *
   * @param id   the unique ID for the administrator
   * @param user the user object associated with the administrator
   */
  public Admin(int id, User user) {
    this.id = id;
    this.user = user;
  }

  /**
   * Returns the user object associated with the administrator.
   *
   * @return the user object
   */
  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
