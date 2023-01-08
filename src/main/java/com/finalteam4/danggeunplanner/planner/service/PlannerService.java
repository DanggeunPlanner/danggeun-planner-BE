package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLANNER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerService {
    private final PlannerRepository plannerRepository;
    private final MemberRepository memberRepository;

    public PlannerResponse find(Member member, String username, String date) {
        Member other = memberRepository.findByUsername(username).orElseThrow(
                ()-> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        Planner planner = plannerRepository.findByMemberAndDate(other, date).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLANNER)
        );

        PlannerResponse response = new PlannerResponse(planner, member);

        for (Plan plan : planner.getPlans()) {
            response.addPlan(plan);
        }

        for (Timer timer : planner.getTimers()) {
            response.addTimer(timer);
        }

        return response;
    }
}
