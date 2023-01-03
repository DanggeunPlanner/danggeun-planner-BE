package com.finalteam4.danggeunplanner.pomodoro.repository;


import com.finalteam4.danggeunplanner.pomodoro.entity.Pomodoro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PomodoroRepository extends JpaRepository<Pomodoro, Long> {
}
