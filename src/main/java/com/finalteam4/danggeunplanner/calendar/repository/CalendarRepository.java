package com.finalteam4.danggeunplanner.calendar.repository;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {
}
