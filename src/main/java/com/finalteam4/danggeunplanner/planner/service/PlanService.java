package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.dto.request.PlanRequest;
import com.finalteam4.danggeunplanner.planner.dto.response.PlanResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLAN;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;

    @Transactional
    public PlanResponse create(Member member, PlanRequest request) {
        Plan plan = request.toEntity(member);
        planRepository.save(plan);
        return new PlanResponse(plan);
    }

    @Transactional
    public PlanResponse update(Member member ,Long planId, PlanRequest request) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLAN)
        );

        Member owner = plan.getMember();

        validateAccess(member,owner);

        updatePlan(plan, request);
        return new PlanResponse(plan);
    }

    private void updatePlan(Plan plan, PlanRequest request) {
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        String content = request.getContent();
        plan.update(startTime, endTime, content);
    }

    @Transactional
    public PlanResponse delete(Member member,Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLAN)
        );

        Member owner = plan.getMember();

        validateAccess(member,owner);

        planRepository.delete(plan);
        return new PlanResponse(plan);
    }

    private void validateAccess(Member member, Member owner){
        if(!member.equals(owner)){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
}
