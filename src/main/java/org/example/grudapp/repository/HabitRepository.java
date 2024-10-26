package org.example.grudapp.repository;

import static org.example.grudapp.repository.DatabaseConstants.PASSWORD;
import static org.example.grudapp.repository.DatabaseConstants.URL;
import static org.example.grudapp.repository.DatabaseConstants.USERNAME;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

public class HabitRepository {

  /**
   * Creates a new habit for the specified user.
   *
   * @param name        the name of the habit
   * @param description the description of the habit
   * @param frequency   the frequency of the habit
   * @throws SQLException if an error occurs while executing the database query
   */
  public void createHabit(String name, String description, String frequency) {

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.CREATE_HABIT)) {

      preparedStatement.setString(1, name);
      preparedStatement.setString(2, description);
      preparedStatement.setString(3, frequency);
      preparedStatement.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          int generatedId = resultSet.getInt(1);
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при создании привычки: " + e.getMessage());
    }
  }

  /**
   * Retrieves a list of habits for the specified user.
   *
   * @param user the user for whom to retrieve habits
   * @return a list of habits for the specified user
   * @throws SQLException if an error occurs while executing the database query
   */
  public List<Habit> getHabitsByUser(User user) {
    List<Habit> result = new ArrayList<>();

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(
            QueryConstants.GET_HABITS_BY_USER)) {

      preparedStatement.setLong(1, user.getId());
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Habit habit = new Habit(
              resultSet.getInt("id"),
              resultSet.getString("name"),
              resultSet.getString("description"),
              resultSet.getString("frequency"),
              user,
              resultSet.getTimestamp("creation_date")
          );
          result.add(habit);
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка при получении привычек: " + e.getMessage());
    }
    return result;
  }

  /**
   * Updates an existing habit with the specified details.
   *
   * @param habitId     the ID of the habit to update
   * @param name        the new name of the habit
   * @param description the new description of the habit
   * @param frequency   the new frequency of the habit
   * @throws SQLException if an error occurs while executing the database query
   */
  public void updateHabit(int habitId, String name, String description, String frequency) {

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.UPDATE_HABIT)) {

      preparedStatement.setString(1, name);
      preparedStatement.setString(2, description);
      preparedStatement.setString(3, frequency);
      preparedStatement.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
      preparedStatement.setInt(5, habitId);

      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Ошибка при обновлении привычки: " + e.getMessage());
    }
  }

  /**
   * Deletes a habit with the specified ID from the database.
   *
   * @param habitId the ID of the habit to delete
   * @throws SQLException if an error occurs while executing the database query
   */
  public void deleteHabit(int habitId) {

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.DELETE_HABIT)) {

      preparedStatement.setInt(1, habitId);
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Error while deleting habit: " + e.getMessage());
    }
  }

  /**
   * Saves a new habit to the database.
   *
   * @param habit the habit to be saved
   * @throws SQLException if an error occurs while executing the database query
   */
  public void saveHabit(Habit habit) {

    try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(QueryConstants.SAVE_HABIT)) {

      preparedStatement.setString(1, habit.getName());
      preparedStatement.setString(2, habit.getDescription());
      preparedStatement.setString(3, habit.getFrequency());
      preparedStatement.setLong(4, habit.getUser().getId());
      preparedStatement.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
      try (ResultSet rs = preparedStatement.executeQuery()) {
        if (rs.next()) {
          int generatedId = rs.getInt(1);
          habit.setId(generatedId);
          habit.getUser().getHabits().add(habit);
        }
      }
    } catch (SQLException e) {
      System.out.println("Ошибка сохранения привычки: " + e.getMessage());
    }
  }
}
