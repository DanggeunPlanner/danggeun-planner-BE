package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlannerResponse {
    private final Boolean isOwner;
    private final String username;
    private final Integer carrot;
    private final List<PlanResponse> plans = new ArrayList<>();
    private final List<TimerResponse> timers = new ArrayList<>();

    public PlannerResponse(Planner planner, Member member){
        this.isOwner= planner.getMember().getId().equals(member.getId());
        this.username = planner.getMember().getUsername();
        this.carrot = planner.getCarrot();
    }

    public void addPlan(PlanResponse response){
        plans.add(response);
    }

    public void addTimer(TimerResponse response){
        timers.add(response);
    }
}
