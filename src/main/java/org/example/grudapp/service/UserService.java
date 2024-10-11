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
}
