package org.example.grudapp.service;

import java.util.HashSet;
import java.util.Set;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;

/**
 * Provides services for managing administrators.
 */
public class AdminService {

  /**
   * Set of all admins. Initially empty.
   */
  private Set<User> admins = new HashSet<>();
  /**
   * Set of all users.
   */
  private Set<User> users = new HashSet<>();

  public Set<Habit> getHabits() {
    return habits;
  }

  /**
   * Set of all habits.
   */
  private Set<Habit> habits = new HashSet<>();

  public AdminService() {
  }

  public AdminService(Set<User> admins, Set<User> users, Set<Habit> habits) {
    this.admins = admins;
    this.users = users;
    this.habits = habits;
  }

  public Set<User> getAdmins() {
    return admins;
  }

  public void setAdmins(Set<User> admins) {
    this.admins = admins;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
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
