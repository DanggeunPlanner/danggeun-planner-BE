package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DIFFERENT_PLANNING_DATE;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_PLANNING_TIME;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.OVERLAP_WITH_OTHER_PLAN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanValidator {
    private final PlanRepository planRepository;

    public void validatePlanningDate(Plan plan) {
        if (!TimeConverter.changeTimeToYearMonthDay(plan.getStartTime()).equals(TimeConverter.changeTimeToYearMonthDay(plan.getEndTime()))) {
            throw new DanggeunPlannerException(DIFFERENT_PLANNING_DATE);
        }
    }

    public void validatePlanningTime(Plan plan) {
        if (plan.getEndTime().isBefore(plan.getStartTime()) ||
                plan.getEndTime().isEqual(plan.getStartTime())) {
            throw new DanggeunPlannerException(NOT_VALID_PLANNING_TIME);
        }
    }

    public void validateOverlapping(Plan plan, Member member) {
        List<Plan> otherPlans = planRepository.findAllByMember(member);
        otherPlans.remove(plan);

        for (Plan otherPlan : otherPlans) {
            if ((otherPlan.getStartTime().isAfter(plan.getStartTime()) && otherPlan.getStartTime().isBefore(plan.getEndTime())) ||
                    (otherPlan.getEndTime().isAfter(plan.getStartTime()) && otherPlan.getEndTime().isBefore(plan.getEndTime())) ||
                    (plan.getStartTime().isAfter(otherPlan.getStartTime()) && plan.getStartTime().isBefore(otherPlan.getEndTime())) ||
                    (plan.getEndTime().isAfter(otherPlan.getStartTime()) && plan.getEndTime().isBefore(otherPlan.getEndTime())) ||
                    (plan.getStartTime().equals(otherPlan.getStartTime()) && plan.getEndTime().equals(otherPlan.getEndTime()))) {
                throw new DanggeunPlannerException(OVERLAP_WITH_OTHER_PLAN);
            }

        }
    }
}
