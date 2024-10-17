package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

/**
 * Provides services for managing habits.
 */
public class HabitService {

  /**
   * Map of all habits. The key is the ID of the habit, and the value is the habit.
   */
  private Map<Integer, Habit> habits = new HashMap<>();

  /**
   * Map of all users. The key is the ID of the user, and the value is the user.
   *
   * @return
   */
  public Map<Integer, Habit> getHabits() {
    return habits;
  }

  public void setHabits(Map<Integer, Habit> habits) {
    this.habits = habits;
  }

  /**
   * Creates a new habit for a given user.
   *
   * @param name
   * @param description
   * @param frequency
   * @param user
   */
  public void createHabit(String name, String description, String frequency, User user) {
    Habit habit = new Habit(habits.size() + 1, name, description, frequency, user, new Date());
    habits.put(habit.getId(), habit);
    user.getHabits().add(habit);
  }

  /**
   * Returns the list of habits for a given user.
   *
   * @param user the user to get habits for
   * @return the list of habits for the user
   */
  public List<Habit> getHabitsByUser(User user) {
    List<Habit> result = new ArrayList<>();
    for (Habit habit : habits.values()) {
      if (habit.getUser().equals(user)) {
        result.add(habit);
      }
    }
    return result;
  }

  /**
   * Updates an existing habit.
   *
   * @param habitId     the ID of the habit to update
   * @param name        the new name of the habit
   * @param description the new description of the habit
   * @param frequency   the new frequency of the habit
   */
  public void updateHabit(int habitId, String name, String description, String frequency) {
    for (Habit habit : habits.values()) {
      if (habit.getId() == habitId) {
        habit.setName(name);
        habit.setDescription(description);
        habit.setFrequency(frequency);
        habit.setCreationDate(new Date());
        return;
      }
    }
  }

  /**
   * Deletes an existing habit.
   *
   * @param habitId the ID of the habit to delete
   */
  public void deleteHabit(int habitId) {
    for (Habit habit : habits.values()) {
      if (habit.getId() == habitId) {
        habits.remove(habit);
        return;
      }
    }
  }
}
