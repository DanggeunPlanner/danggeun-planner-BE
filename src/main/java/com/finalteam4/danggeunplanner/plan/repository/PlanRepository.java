package com.finalteam4.danggeunplanner.plan.repository;

import com.finalteam4.danggeunplanner.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,Long> {
}
