package org.example.grudapp.service;

import java.util.List;
import java.util.Map;
import org.example.grudapp.model.Role;
import org.example.grudapp.model.User;
import org.example.grudapp.repository.UserRepository;

/**
 * Provides services for managing users.
 */
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieves a list of all users from the database.
   *
   * @return A list of User objects.
   */
  public List<User> getUsers() {
    return userRepository.getUsers().values().stream().toList();
  }

  /**
   * Retrieves a user by their email.
   *
   * @param email The email of the user to retrieve.
   * @return The User object, or null if the user is not found.
   */
  public User getUserByEmail(String email) {
    return userRepository.getUserByEmail(email);
  }

  public User registerUser(String email, String password, String name) {
    return userRepository.registerUser(email, password, name);
  }

  /**
   * Retrieves a user by their ID.
   *
   * @param id The ID of the user to retrieve.
   * @return The User object, or null if the user is not found.
   */
  public User getUserById(int id) {
    return userRepository.getUserById(id);
  }

  /**
   * Creates a new user in the database.
   *
   * @param user The User object to create.
   */
  public void createUser(User user) {
    userRepository.createUser(user);
  }

  /**
   * Updates a user's profile in the database.
   *
   * @param user The User object containing the updated user data.
   */
  public void editUserProfile(User user) {
    userRepository.editUserProfile(user);
  }

  /**
   * Resets a user's password via their email.
   *
   * @param email The email of the user to reset the password for.
   */
  public void resetPassword(String email) {
    userRepository.resetPassword(email);
  }

  /**
   * Authenticates a user by their email and password.
   *
   * @param email    The email of the user to authenticate.
   * @param password The password of the user to authenticate.
   * @return The authenticated User object, or null if the user is not found or the password is
   * incorrect.
   */
  public User authenticatedUser(String email, String password) {
    return userRepository.authenticateUser(email, password);
  }


  /**
   * Deletes a user from the database by their email.
   *
   * @param email The email of the user to delete.
   */
  public void deleteUser(String email) {
    userRepository.deleteUser(email);
  }

  /**
   * Assigns the ADMIN role to a user and adds them to the collection of admins.
   *
   * @param user The User object to assign the ADMIN role to.
   */
  public void assignAdminRole(User user) {
    userRepository.assignAdminRole(user);
  }

  /**
   * Assigns the USER role to a user and removes them from the collection of admins.
   *
   * @param user The User object to assign the USER role to.
   */
  public void assignUserRole(User user) {
    userRepository.assignUserRole(user);
  }

  /**
   * Retrieves a map of all users with a specific role.
   *
   * @param role The role to search for.
   * @return A map where the keys are user IDs and the values are User objects.
   */
  public Map<Integer, User> getUsersByRole(String role) {
    return userRepository.getUsersByRole(Role.valueOf(role.toUpperCase()));
  }
}

