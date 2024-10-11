package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

/**
 * Provides services for managing habits.
 */
public class HabitService {

  /**
   * List of all habits.
   */
  private List<Habit> habits = new ArrayList<>();

  /**
   * Returns the list of all habits.
   *
   * @return the list of habits
   */
  public List<Habit> getHabits() {
    return habits;
  }

  public void setHabits(List<Habit> habits) {
    this.habits = habits;
  }

  /**
   * Creates a new habit.
   *
   * @param name        the name of the habit
   * @param description the description of the habit
   * @param frequency   the frequency of the habit
   * @param user        the user who created the habit
   * @param habits      the list of habits to add the new habit to
   */
  public void createHabit(String name, String description, String frequency, User user,
      List<Habit> habits) {
    Habit habit = new Habit(habits.size() + 1, name, description, frequency, user, new Date());
    habits.add(habit);
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
    for (Habit habit : habits) {
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
    for (Habit habit : habits) {
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
    for (Habit habit : habits) {
      if (habit.getId() == habitId) {
        habits.remove(habit);
        return;
      }
    }
  }
}
