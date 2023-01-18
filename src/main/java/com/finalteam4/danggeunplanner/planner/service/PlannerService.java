package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.dto.response.PlanResponse;
import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerService {
    private final PlannerRepository plannerRepository;
    private final MemberRepository memberRepository;

    public PlannerResponse findAll(Member member, String username, String date) {
        Member other = memberRepository.findByUsername(username).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Optional<Planner> planner = plannerRepository.findByMemberAndDate(other, date);

        if (planner.isPresent()) {
            PlannerResponse response = new PlannerResponse(planner.get(), member);
            for (Plan plan : planner.get().getPlans()) {
                response.addPlan(new PlanResponse(plan));
            }
            for (Timer timer : planner.get().getTimers()) {
                response.addTimer(new TimerResponse(timer));
            }
            return response;
        }
        return new PlannerResponse(other,member);
    }

    public PlannerResponse findPlan(Member member, String username, String date) {
        Member other = memberRepository.findByUsername(username).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Optional<Planner> planner = plannerRepository.findByMemberAndDate(other, date);

        if (planner.isPresent()) {
            PlannerResponse response = new PlannerResponse(planner.get(), member);
            for (Plan plan : planner.get().getPlans()) {
                response.addPlan(new PlanResponse(plan));
            }
            return response;
        }
        return new PlannerResponse(other,member);
    }

    public PlannerResponse findTimer(Member member, String username, String date) {
        Member other = memberRepository.findByUsername(username).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Optional<Planner> planner = plannerRepository.findByMemberAndDate(other, date);

        if (planner.isPresent()) {
            PlannerResponse response = new PlannerResponse(planner.get(), member);
            for (Timer timer : planner.get().getTimers()) {
                response.addTimer(new TimerResponse(timer));
            }
            return response;
        }
        return new PlannerResponse(other,member);
    }
}
