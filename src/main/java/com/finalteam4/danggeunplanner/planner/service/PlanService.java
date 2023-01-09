package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
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
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final PlannerRepository plannerRepository;
    @Transactional
    public PlanResponse create(Member member, PlanRequest request) {
        Plan plan = request.toPlan(member);
        planRepository.save(plan);

        String date = plan.getDate();

        createPlanner(member,date);
        Planner planner = plannerRepository.findByMemberAndDate(member,date).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLANNER));
        plan.confirmPlanner(planner);

        return new PlanResponse(plan);
    }

    private void createPlanner(Member member,String date){
        if(!plannerRepository.existsByMemberAndDate(member, date)) {
            Planner planner = new Planner(member,date);
            plannerRepository.save(planner);
        }
    }

    @Transactional
    public PlanResponse update(Member member ,Long planId, PlanRequest request) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLAN));
        isValidatedMember(member,plan.getMember());

        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        String content = request.getContent();

        plan.update(startTime, endTime, content);
        return new PlanResponse(plan);
    }

    @Transactional
    public PlanResponse delete(Member member,Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLAN));
        isValidatedMember(member,plan.getMember());

        planRepository.delete(plan);
        return new PlanResponse(plan);
    }

    private void isValidatedMember(Member member, Member other){
        if(!member.getId().equals(other.getId())){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
}
