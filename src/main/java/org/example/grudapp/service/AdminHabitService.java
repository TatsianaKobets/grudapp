package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

public class AdminHabitService {

  /**
   * List of all habits in the system.
   */
  private List<Habit> habitsInSystem = new ArrayList<>();

  /**
   * Returns the list of all habits in the system.
   *
   * @return the list of habits
   */
  public List<Habit> getHabitsInSystem() {
    return habitsInSystem;
  }

  public void setHabitsInSystem(List<Habit> habitsInSystem) {
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
    habitsInSystem.add(habit);
  }
}
