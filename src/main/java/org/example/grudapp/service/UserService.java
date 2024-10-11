package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.List;
import org.example.grudapp.model.User;

public class UserService {

  private List<User> users = new ArrayList<>();
  private User authenticatedUser;

  public User getAuthenticatedUser() {
    return authenticatedUser;
  }

  public void registerUser(String email, String password, String name) {
    User user = new User(users.size() + 1, email, password, name);
    users.add(user);
  }

  public User authenticateUser(String email, String password) {
    for (User user : users) {
      if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
        authenticatedUser = user;
        return user;
      }
    }
    return null;
  }

  public User getUserByEmail(String email) {
    for (User user : users) {
      if (user.getEmail().equals(email)) {
        return user;
      }
    }
    return null;
  }

  public void editUserProfile(User user) {
    // обновить данные пользователя в базе данных
    for (User existingUser : users) {
      if (existingUser.getId() == user.getId()) {
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        break;
      }
    }
  }

  public User getUserById(int id) {
    for (User user : users) {
      if (user.getId() == id) {
        return user;
      }
    }
    return null;
  }

  public void deleteUser(String email) {
    for (User user : users) {
      if (user.getEmail() == email) {
        users.remove(user);
        break;
      }
    }
  }

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

  public String generatePassword() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder password = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      password.append(characters.charAt((int) (Math.random() * characters.length())));
    }
    return password.toString();
  }
}
