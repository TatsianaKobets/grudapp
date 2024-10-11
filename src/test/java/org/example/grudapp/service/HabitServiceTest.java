package org.example.grudapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HabitServiceTest {

  private User user;
  private HabitService habitService;
  private List<Habit> habits;

  @BeforeEach
  void setUp() {
    user = new User(1, "John Doe", "johndoe", "password123");
    habits = new ArrayList<>();
    habitService = new HabitService();
    habitService.createHabit("Test Habit", "This is a test habit", "daily", user, habits);
    habitService.setHabits(habits);
  }

  @Test
  void getHabits() {
    List<Habit> habits = habitService.getHabits();
    assertEquals(1, habits.size());
  }

  @Test
  void getHabitsByUser() {
    List<Habit> habitsByUser = habitService.getHabitsByUser(user);
    assertEquals(1, habitsByUser.size());
    assertEquals(user, habitsByUser.get(0).getUser());
  }

  @Test
  void createHabit() {
    habitService.createHabit("New Habit", "This is a new habit", "weekly", user, habits);
    List<Habit> updatedHabits = habitService.getHabits();
    assertEquals(2, updatedHabits.size());
    Habit newHabit = updatedHabits.get(1);
    assertEquals("New Habit", newHabit.getName());
    assertEquals("This is a new habit", newHabit.getDescription());
    assertEquals("weekly", newHabit.getFrequency());
    assertEquals(user, newHabit.getUser());
    Instant instant = newHabit.getCreationDate().toInstant();
    assertTrue(ChronoUnit.HOURS.between(instant, Instant.now()) <= 1);
  }

  @Test
  void updateHabit() {
    Habit habitToUpdate = habits.get(0);
    habitService.updateHabit(habitToUpdate.getId(), "Updated Habit", "This is an updated habit",
        "monthly");
    Habit updatedHabit = habitService.getHabits().get(0);
    assertEquals("Updated Habit", updatedHabit.getName());
    assertEquals("This is an updated habit", updatedHabit.getDescription());
    assertEquals("monthly", updatedHabit.getFrequency());
  }

  @Test
  void deleteHabit() {
    Habit habitToDelete = habits.get(0);
    habitService.deleteHabit(habitToDelete.getId());
    List<Habit> updatedHabits = habitService.getHabits();
    assertEquals(0, updatedHabits.size());
  }
}
