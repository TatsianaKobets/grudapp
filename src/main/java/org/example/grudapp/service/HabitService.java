package org.example.grudapp.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.grudapp.model.Habit;
import org.example.grudapp.model.User;
import org.example.grudapp.repository.HabitRepository;

/**
 * Provides services for managing habits.
 */
public class HabitService {

  private HabitRepository habitRepository;
  private Map<Integer, Habit> habits = new HashMap<>();


  public Map<Integer, Habit> getHabits() {
    return habits;
  }

  public HabitService(HabitRepository habitRepository) {
    this.habitRepository = habitRepository;
  }

  public void createHabit(String name, String description, String frequency) {
    habitRepository.createHabit(name, description, frequency);
  }

  public List<Habit> getHabitsByUser(User user) {
    return habitRepository.getHabitsByUser(user);
  }

  public void updateHabit(Habit habit) throws SQLException {
    habitRepository.saveHabit(habit);
  }

  public void deleteHabit(int habitId) throws SQLException {
    habitRepository.deleteHabit(habitId);
  }


}
