package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.service.MemberValidator;
import com.finalteam4.danggeunplanner.planner.dto.request.PlanRequest;
import com.finalteam4.danggeunplanner.planner.dto.response.PlanResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLAN;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLANNER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final PlannerRepository plannerRepository;
    private final PlanValidator planValidator;
    private final MemberValidator memberValidator;

    @Transactional
    public PlanResponse create(Member member, PlanRequest request) {
        Plan plan = request.toPlan(member);

        planValidator.validatePlanningDate(plan);
        planValidator.validatePlanningTime(plan);
        planValidator.validateOverlapping(plan, member);

        planRepository.save(plan);

        createPlanner(member, plan);
        Planner planner = plannerRepository.findByMemberAndDate(member, plan.getDate()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLANNER)
        );
        plan.confirmPlanner(planner);

        return new PlanResponse(plan);
    }

    @Transactional
    public PlanResponse update(Member member, Long planId, PlanRequest request) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLAN)
        );
        memberValidator.validateAccess(member, plan.getMember());

        plan.update(request.getStartTime(), request.getEndTime(), request.getContent());

        planValidator.validatePlanningTime(plan);
        planValidator.validatePlanningDate(plan);
        planValidator.validateOverlapping(plan, member);

        return new PlanResponse(plan);
    }

    @Transactional
    public PlanResponse delete(Member member, Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLAN)
        );
        memberValidator.validateAccess(member, plan.getMember());

        planRepository.delete(plan);
        return new PlanResponse(plan);
    }

    private void createPlanner(Member member, Plan plan) {
        if (!plannerRepository.existsByMemberAndDate(member, plan.getDate())) {
            Planner planner = new Planner(member, plan.getDate());
            plannerRepository.save(planner);
        }
    }
}
