package org.example.grudapp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.example.grudapp.model.Habit;

public class AdminHabitService {

  /**
   * Map of all habits in the system. The key is the ID of the habit, and the value is the habit.
   */
  private Map<Integer, Habit> habitsInSystem = new HashMap<>();

  /**
   * Map of all users in the system. The key is the ID of the user, and the value is the user.
   *
   * @return
   */
  public Map<Integer, Habit> getHabitsInSystem() {
    return habitsInSystem;
  }

  public void setHabitsInSystem(Map<Integer, Habit> habitsInSystem) {
    this.habitsInSystem = habitsInSystem;
  }

  /**
   * Creates a new habit for from the role administrator in the system.
   *
   * @param name        the name of the habit
   * @param description the description of the habit
   */
  public void createHabitInSystem(String name, String description) {
    Habit habit = new Habit(habitsInSystem.size() + 1, name, description, new Date());
    habitsInSystem.put(habit.getId(), habit);
  }
}
