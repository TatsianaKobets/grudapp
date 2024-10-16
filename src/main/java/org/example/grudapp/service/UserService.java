package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.List;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;

/**
 * Provides services for managing users.
 */
public class UserService {

  /**
   * List of all users.
   */
  private List<User> users = new ArrayList<>();
  /**
   * The currently authenticated user.
   */
  private User authenticatedUser;

  /**
   * Returns the list of all users.
   *
   * @return the list of users
   */
  public List<User> getUsers() {
    return users;
  }

  /**
   * Returns the currently authenticated user.
   *
   * @return the authenticated user
   */
  public User getAuthenticatedUser() {
    return authenticatedUser;
  }

  /**
   * Registers a new user.
   *
   * @param email    the email address of the user
   * @param password the password of the user
   * @param name     the name of the user
   */
  public void registerUser(String email, String password, String name) {
    User user = new User(users.size() + 1, email, password, name);
    user.setRole(Role.USER);
    users.add(user);
  }

  /**
   * Authenticates a user.
   *
   * @param email    the email address of the user
   * @param password the password of the user
   * @return the authenticated user, or null if authentication fails
   */
  public User authenticateUser(String email, String password) {
    for (User user : users) {
      if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
        authenticatedUser = user;
        return user;
      }
    }
    return null;
  }

  /**
   * Returns a user by their ID.
   *
   * @param id the ID of the user
   * @return the user, or null if not found
   */
  public User getUserById(int id) {
    for (User user : users) {
      if (user.getId() == id) {
        return user;
      }
    }
    return null;
  }

  /**
   * Returns a user by their email address.
   *
   * @param email the email address of the user
   * @return the user, or null if not found
   */
  public User getUserByEmail(String email) {
    for (User user : users) {
      if (user.getEmail().equals(email)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Edits a user's profile.
   *
   * @param user the user to edit
   */
  public void editUserProfile(User user) {
    for (User existingUser : users) {
      if (existingUser.getId() == user.getId()) {
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        break;
      }
    }
  }

  /**
   * Resets a user's password.
   *
   * @param email the email address of the user
   */
  public void resetPassword(String email) {
    String newPassword = generatePassword();
    User user = getUserByEmail(email);
    if (user != null) {
      user.setPassword(newPassword);
      System.out.println("Новый пароль: " + newPassword);
    } else {
      System.out.println("Пользователь с email " + email + " не найден");
    }
  }

  /**
   * Generates a random password.
   *
   * @return the generated password
   */
  public String generatePassword() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder password = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      password.append(characters.charAt((int) (Math.random() * characters.length())));
    }
    return password.toString();
  }

  /**
   * Deletes a user by their email address.
   *
   * @param email the email address of the user to delete
   */
  public void deleteUser(String email) {
    for (User user : users) {
      if (user.getEmail() == email) {
        users.remove(user);
        break;
      }
    }
  }
}
