package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import com.finalteam4.danggeunplanner.pomodoro.entity.Pomodoro;
import com.finalteam4.danggeunplanner.pomodoro.repository.PomodoroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerService {
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PomodoroRepository pomodoroRepository;

    public PlannerResponse find(Long memberId, Long searchId, String date) {

        boolean isOwner = false;
        if (memberId.equals(searchId)) {
            isOwner = true;
        }

        List<Plan> plans = planRepository.findAllByDateAndId(date, memberId);
        List<Pomodoro> pomodoros = pomodoroRepository.findAllByDateAndId(date, memberId);

        Integer todayCarrot = pomodoros.size();

        PlannerResponse response = new PlannerResponse(isOwner, todayCarrot);

        for (Plan plan : plans) {
            response.addPlan(plan);
        }

        for (Pomodoro pomodoro : pomodoros) {
            response.addPomodoro(pomodoro);
        }

        return response;
    }
}
