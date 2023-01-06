package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.dto.request.PlanRequest;
import com.finalteam4.danggeunplanner.planner.dto.response.PlanResponse;
import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLAN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final TimerRepository pomodoroRepository;

    public PlannerResponse find(Long memberId, Long searchId, String date) {

        boolean isOwner = false;
        if (memberId.equals(searchId)) {
            isOwner = true;
        }

        List<Plan> plans = planRepository.findAllByDateAndId(date, memberId);
        List<com.finalteam4.danggeunplanner.timer.entity.Timer> pomodoros = pomodoroRepository.findAllByDateAndId(date, memberId);

        Integer todayCarrot = pomodoros.size();

        PlannerResponse response = new PlannerResponse(isOwner, todayCarrot);

        for (Plan plan : plans) {
            response.addPlan(plan);
        }

        for (com.finalteam4.danggeunplanner.timer.entity.Timer pomodoro : pomodoros) {
            response.addPomodoro(pomodoro);
        }

        return response;
    }

    @Transactional
    public PlanResponse createPlan(Long memberId, PlanRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_MEMBER));
        Plan plan = request.toEntity(member);
        planRepository.save(plan);
        return new PlanResponse(plan);
    }

    @Transactional
    public PlanResponse updatePlan(Long planId, PlanRequest request) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLAN));
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
    public PlanResponse deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLAN));
        planRepository.delete(plan);
        return new PlanResponse(plan);
    }

}
