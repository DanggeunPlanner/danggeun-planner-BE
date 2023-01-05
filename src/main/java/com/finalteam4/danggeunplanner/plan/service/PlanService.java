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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PlanInfoResponse createPlan(Long id, PlanInfoRequest request){
        Member member = memberRepository.findById(id).orElseThrow(()-> new DanggeunPlannerException(NOT_FOUND_MEMBER));
        Plan plan = request.toEntity(member);
        planRepository.save(plan);
        return new PlanInfoResponse(plan);
    }
}
