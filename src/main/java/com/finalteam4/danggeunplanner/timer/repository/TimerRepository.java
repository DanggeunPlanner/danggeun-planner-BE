package com.finalteam4.danggeunplanner.timer.repository;


import com.finalteam4.danggeunplanner.timer.entity.Timer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findAllByDateAndId(String date, Long id);
}
