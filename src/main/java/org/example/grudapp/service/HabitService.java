package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.List;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

public class HabitService {
  private List<Habit> habits = new ArrayList<>();
  public List<Habit> getHabits() {
    return habits;
  }
  public void createHabit(String name, String description, String frequency, User user) {
    Habit habit = new Habit(habits.size() + 1, name, description, frequency, user);
    habits.add(habit);
    user.getHabits().add(habit);
  }

  public void updateHabit(int habitId, String name, String description, String frequency) {
    for (Habit habit : habits) {
      if (habit.getId() == habitId) {
        habit.setName(name);
        habit.setDescription(description);
        habit.setFrequency(frequency);
        return;
      }
    }
  }

  public void deleteHabit(int habitId) {
    for (Habit habit : habits) {
      if (habit.getId() == habitId) {
        habits.remove(habit);
        return;
      }
    }
  }
}
