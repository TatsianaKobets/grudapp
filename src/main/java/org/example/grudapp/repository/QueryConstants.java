package org.example.grudapp.repository;

public class QueryConstants {

  public static final String CREATE_HABIT = "INSERT INTO postgres_schema.habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";
  public static final String GET_HABITS_BY_USER = "SELECT * FROM postgres_schema.habits WHERE user_id = ?";
  public static final String UPDATE_HABIT = "UPDATE postgres_schema.habits SET name = ?, description = ?, frequency = ?, creation_date = ? WHERE id = ?";
  public static final String DELETE_HABIT = "DELETE FROM postgres_schema.habits WHERE id = ?";
  public static final String SAVE_HABIT = "INSERT INTO postgres_schema.habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";
  public static final String CREATE_LOG = "INSERT INTO postgres_schema.logs (log_date, completed, habit_id, user_id) VALUES (?, ?, ?, ?) RETURNING id";
  public static final String GET_LOGS_BY_USER = "SELECT * FROM postgres_schema.logs WHERE user_id = ?";
  public static final String UPDATE_LOG = "UPDATE postgres_schema.logs SET log_date = ?, completed = ?, habit_id = ? WHERE id = ?";
  public static final String DELETE_LOG = "DELETE FROM postgres_schema.logs WHERE id = ?";
  public static final String GET_STREAK = "SELECT * FROM postgres_schema.logs WHERE habit_id = ? ORDER BY log_date";
  public static final String GET_SUCCESS_PERCENTAGE = "SELECT * FROM postgres_schema.logs WHERE habit_id = ? AND log_date BETWEEN ? AND ?";
  public static final String ADD_LOG = "INSERT INTO postgres_schema.logs (log_date, completed, habit_id, user_id) VALUES (?, ?, ?, ?) RETURNING id";
  public static final String GET_LOGS_BY_HABIT = "SELECT * FROM postgres_schema.logs WHERE habit_id = ?";
  public static final String SAVE_LOG = "INSERT INTO postgres_schema.logs (log_date, completed, habit_id, user_id) VALUES (?, ?, ?, ?) RETURNING id";

  public static final String CREATE_NOTIFICATION = "INSERT INTO postgres_schema.notifications (notification_date, sent, user_id, habit_id) VALUES (?, ?, ?, ?)";
  public static final String GET_NOTIFICATION_COUNT = "SELECT * FROM postgres_schema.notifications";
  public static final String ADD_NOTIFICATION = "INSERT INTO postgres_schema.notifications (notification_date, sent, user_id, habit_id) VALUES (?, ?, ?, ?)";
  public static final String SEND_NOTIFICATION = "SELECT * FROM postgres_schema.notifications WHERE id = ?";
  public static final String UPDATE_NOTIFICATION = "UPDATE postgres_schema.notifications SET sent = ? WHERE id = ?";
  public static final String GET_USERS = "SELECT * FROM postgres_schema.users";
  public static final String GET_USER_BY_EMAIL = "SELECT * FROM postgres_schema.users WHERE email = ?";
  public static final String GET_USER_BY_ID = "SELECT * FROM postgres_schema.users WHERE id = ?";
  public static final String CREATE_USER = "INSERT INTO postgres_schema.users (email, password, name, role) VALUES (?, ?, ?, ?) RETURNING id";
  public static final String EDIT_USER_PROFILE = "UPDATE postgres_schema.users SET email = ?, password = ? WHERE id = ?";
  public static final String REGISTER_USER = "INSERT INTO postgres_schema.users (email, password, name, role) VALUES (?, ?, ?, ?) RETURNING id";
  public static final String AUTHENTICATE_USER = "SELECT * FROM postgres_schema.users WHERE email = ? AND password = ?";
  public static final String DELETE_USER = "DELETE FROM postgres_schema.users WHERE email = ?";
  public static final String UPDATE_USER_ROLE = "UPDATE postgres_schema.users SET role = ? WHERE email = ?";
  public static final String GET_USERS_BY_ROLE = "SELECT * FROM postgres_schema.users WHERE role = ?";
}
