package com.finalteam4.danggeunplanner.planner.repository;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner,Long> {
    Boolean existsByMemberAndDate(Member member, String date);
    Optional<Planner> findByMemberAndDate(Member member, String date);

}
