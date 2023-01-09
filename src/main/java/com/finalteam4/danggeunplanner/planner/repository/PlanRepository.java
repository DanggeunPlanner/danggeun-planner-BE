package com.finalteam4.danggeunplanner.planner.repository;

import com.finalteam4.danggeunplanner.planner.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,Long> {
}
