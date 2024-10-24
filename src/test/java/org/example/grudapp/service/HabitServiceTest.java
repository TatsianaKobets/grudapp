package org.example.grudapp.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.grudapp.PostgresContainerTest;
import org.example.grudapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.TestcontainersExtension;

@ExtendWith(TestcontainersExtension.class)
public class HabitServiceTest extends PostgresContainerTest {

  private HabitService habitService;

  @BeforeEach
  void setUp() {
    habitService = new HabitService();
  }

  @Test
  public void testCreateHabit_success() throws SQLException {
    String name = "Test Habit";
    String description = "This is a test habit";
    String frequency = "DAILY";
    User user = new User( "2@2", "2222", "2");

    habitService.createHabit(name, description, frequency, user);
    assertTrue(habitService.getHabits().containsKey(3));
  }
  @Test
  public void testCreateHabit_emptyName() {
    String name = "";
    String description = "This is a test habit";
    String frequency = "DAILY";
    User user = new User(1, "test@example.com", "test", "test");

    assertThrows(SQLException.class, () -> habitService.createHabit(name, description, frequency, user));
  }

  @Test
  public void testCreateHabit_nullUser() {
    String name = "Test Habit";
    String description = "This is a test habit";
    String frequency = "DAILY";
    User user = null;

    assertThrows(NullPointerException.class, () -> habitService.createHabit(name, description, frequency, user));
  }

}
