package com.finalteam4.danggeunplanner.pomodoro.repository;


import com.finalteam4.danggeunplanner.pomodoro.entity.Pomodoro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PomodoroRepository extends JpaRepository<Pomodoro, Long> {
    List<Pomodoro> findAllByDateAndId(String date,Long id);
}
