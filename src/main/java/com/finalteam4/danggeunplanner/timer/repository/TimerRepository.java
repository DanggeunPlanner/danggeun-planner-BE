package com.finalteam4.danggeunplanner.timer.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimerRepository extends JpaRepository<com.finalteam4.danggeunplanner.timer.entity.Timer, Long> {
    List<com.finalteam4.danggeunplanner.timer.entity.Timer> findAllByDateAndId(String date, Long id);
}
