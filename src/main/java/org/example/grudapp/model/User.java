package org.example.grudapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a user in the system.
 */
public class User {

  /**
   * Unique identifier for the user.
   */
  private int id;
  /**
   * Email address of the user.
   */
  private String email;
  /**
   * Password of the user.
   */
  private String password;
  /**
   * Name of the user.
   */
  private String name;
  /**
   * List of habits associated with the user.
   */
  private List<Habit> habits;
  /**
   * List of logs associated with the user.
   */
  private List<Log> logs;
  /**
   * Indicates whether the user is blocked.
   */
  private boolean blocked;
  /**
   * Role of the user (ADMIN or USER).
   */
  private Role role;
  public Map<Integer, User> users = new HashMap<>();

  public User(int id) {
    this.id = id;
  }

  public User() {
  }
  /**
   * Constructs a User object with the given parameters.
   *
   * @param id       the unique ID for the user
   * @param email    the email address of the user
   * @param password the password of the user
   * @param name     the name of the user
   * @param role     the role of the user
   */
  public User(int id, String email, String password, String name, Role role) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.habits = new ArrayList<>();
    this.logs = new ArrayList<>();
    this.blocked = false;
    this.role = role;
  }
  /**
   * Constructs a User object with the given parameters.
   *
   * @param id       the unique ID for the user
   * @param email    the email address of the user
   * @param password the password of the user
   * @param name     the name of the user
   */
  public User(int id, String email, String password, String name) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.habits = new ArrayList<>();
    this.logs = new ArrayList<>();
  }
  /**
   * Constructs a User object with the given parameters.
   *
   * @param email    the email address of the user
   * @param password the password of the user
   * @param name     the name of the user
   */
  public User(String email, String password, String name) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.habits = new ArrayList<>();
    this.logs = new ArrayList<>();
  }


  /**
   * Returns the unique ID for the user.
   *
   * @return the ID
   */
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
  /**
   * Returns the email address of the user.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the password of the user.
   *
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the name of the user.
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
   * Returns the list of habits associated with the user.
   *
   * @return the list of habits
   */
  public List<Habit> getHabits() {
    return habits;
  }

  /**
   * Returns the list of logs associated with the user.
   *
   * @return the list of logs
   */
  public List<Log> getLogs() {
    return logs;
  }

  /**
   * Returns whether the user is blocked.
   *
   * @return true if blocked, false otherwise
   */
  public boolean isBlocked() {
    return blocked;
  }

  /**
   * Sets whether the user is blocked.
   *
   * @param blocked true to block the user, false to unblock
   */
  public void setBlocked(boolean blocked) {
    this.blocked = blocked;
  }

  /**
   * Returns the role of the user.
   *
   * @return the role
   */
  public Role getRole() {
    return role;
  }

  /**
   * Sets the role of the user.
   *
   * @param role the new role
   */
  public void setRole(Role role) {
    this.role = role;
  }

  public Map<Integer, User> getUsers() {
    return users;
  }

  public void setUsers(Map<Integer, User> users) {
    this.users = users;
  }
}
