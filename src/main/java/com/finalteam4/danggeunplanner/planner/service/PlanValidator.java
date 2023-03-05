package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.DIFFERENT_PLANNING_DATE;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_PLANNING_TIME;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanValidator {

    public void validatePlanningDate(Plan plan) {
        if (!TimeConverter.convertToPlannerDateForm(plan.getStartTime()).equals(TimeConverter.convertToPlannerDateForm(plan.getEndTime()))) {
            throw new DanggeunPlannerException(DIFFERENT_PLANNING_DATE);
        }
    }

    public void validatePlanningTime(Plan plan) {
        if (plan.getEndTime().isBefore(plan.getStartTime()) ||
                plan.getEndTime().isEqual(plan.getStartTime())) {
            throw new DanggeunPlannerException(NOT_VALID_PLANNING_TIME);
        }
    }
}
