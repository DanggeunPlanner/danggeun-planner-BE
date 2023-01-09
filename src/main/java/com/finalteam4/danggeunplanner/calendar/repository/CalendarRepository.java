package com.finalteam4.danggeunplanner.calendar.repository;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    boolean existsByMemberAndDate(Member member, String date);
    Optional<Calendar> findByMemberAndDate(Member member, String date);
}
