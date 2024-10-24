package org.example.grudapp.repository;

public class QueryConstants {
  public static final String CREATE_HABIT = "INSERT INTO postgres_schema.habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";
  public static final String GET_HABITS_BY_USER = "SELECT * FROM postgres_schema.habits WHERE user_id = ?";
  public static final String UPDATE_HABIT = "UPDATE postgres_schema.habits SET name = ?, description = ?, frequency = ?, creation_date = ? WHERE id = ?";
  public static final String DELETE_HABIT = "DELETE FROM postgres_schema.habits WHERE id = ?";
  public static final String SAVE_HABIT = "INSERT INTO postgres_schema.habits (name, description, frequency, user_id, creation_date) VALUES (?, ?, ?, ?, ?) RETURNING id";
}
