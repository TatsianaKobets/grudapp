package org.example.grudapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;

public class HabitService {

  private List<Habit> habits = new ArrayList<>();

  public List<Habit> getHabits() {
    return habits;
  }

  public void createHabit(String name, String description, String frequency, User user, List<Habit> habits) {
    Habit habit = new Habit(habits.size() + 1, name, description, frequency, user, new Date());
    habits.add(habit);
    user.getHabits().add(habit);
  }

  public List<Habit> getHabitsByUser(User user) {
    List<Habit> result = new ArrayList<>();
    for (Habit habit : habits) {
      if (habit.getUser().equals(user)) {
        result.add(habit);
      }
    }
    return result;
  }
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

  public void deleteHabit(int habitId) {
    for (Habit habit : habits) {
      if (habit.getId() == habitId) {
        habits.remove(habit);
        return;
      }
    }
  }
}
