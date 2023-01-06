package com.finalteam4.danggeunplanner.planner.service;

import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlannerService {
    private final PlanRepository planRepository;
    private final TimerRepository timerRepository;

    public PlannerResponse find(Long memberId, Long searchId, String date) {


        boolean owner = memberId.equals(searchId);
        List<Plan> plans = planRepository.findAllByDateAndId(date, memberId);
        List<Timer> timers = timerRepository.findAllByDateAndId(date, memberId);

        Integer todayCarrot = timers.size();

        PlannerResponse response = new PlannerResponse(owner, todayCarrot);

        for (Plan plan : plans) {
            response.addPlan(plan);
        }

        for (Timer timer : timers) {
            response.addPomodoro(timer);
        }

        return response;
    }
}
