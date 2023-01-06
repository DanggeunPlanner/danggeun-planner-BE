package com.finalteam4.danggeunplanner.plan.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.plan.dto.request.PlanInfoRequest;
import com.finalteam4.danggeunplanner.plan.dto.response.PlanInfoResponse;
import com.finalteam4.danggeunplanner.plan.entity.Plan;
import com.finalteam4.danggeunplanner.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLAN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PlanInfoResponse create(Long memberId, PlanInfoRequest request){
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new DanggeunPlannerException(NOT_FOUND_MEMBER));
        Plan plan = request.toEntity(member);
        planRepository.save(plan);
        return new PlanInfoResponse(plan);
    }

    @Transactional
    public PlanInfoResponse update(Long planId, PlanInfoRequest request){
        Plan plan = planRepository.findById(planId).orElseThrow(()-> new DanggeunPlannerException(NOT_FOUND_PLAN));
        updatePlan(plan, request);
        return new PlanInfoResponse(plan);
    }

    private void updatePlan(Plan plan, PlanInfoRequest request){
        String startTime = request.getStartTime();
        String endTime = request.getEndTime();
        String content = request.getContent();
        plan.update(startTime, endTime, content);
    }

    @Transactional
    public PlanInfoResponse delete(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(()-> new DanggeunPlannerException(NOT_FOUND_PLAN));
        planRepository.delete(plan);
        return new PlanInfoResponse(plan);
    }
}
