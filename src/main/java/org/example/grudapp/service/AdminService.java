package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;

/**
 * Provides services for managing administrators.
 */public class AdminService {
  /**
   * List of all administrators.
   */
  private List<User> admins = new ArrayList<>();
  /**
   * List of all users.
   */
  private List<User> users = new ArrayList<>();
  /**
   * List of all habits.
   */
  private List<Habit> habits = new ArrayList<>();

  public List<User> getAdmins() {
    return admins;
  }

  public AdminService(List<User> admins, List<User> users, List<Habit> habits) {
    this.admins = admins;
    this.users = users;
    this.habits = habits;
  }
  public List<User> getUsers() {
    return users;
  }

  public List<Habit> getHabits() {
    return habits;
  }
  public void addUser(User user) {
    admins.add(user);
    users.add(user);
  }
  public void removeUser(User user) {
    admins.remove(user);
    users.remove(user);
  }

  public void addHabit(Habit habit) {
    habits.add(habit);
  }
  public void removeHabit(Habit habit) {
    habits.remove(habit);
  }

  /**
   * Assigns the admin role to a user.
   *
   * @param user the user to assign the admin role to
   */
  public void assignAdminRole(User user) {
    user.setRole(Role.ADMIN);
    admins.add(user);
  }
  /**
   * Assigns the user role to a user.
   *
   * @param user the user to assign the user role to
   */
  public void assignUserRole(User user) {
    user.setRole(Role.USER);
    admins.remove(user);
  }
}
