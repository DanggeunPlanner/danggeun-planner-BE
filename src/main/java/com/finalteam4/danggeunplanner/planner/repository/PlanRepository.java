package com.finalteam4.danggeunplanner.planner.repository;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan,Long> {
    List<Plan> findAllByMember(Member member);
}
