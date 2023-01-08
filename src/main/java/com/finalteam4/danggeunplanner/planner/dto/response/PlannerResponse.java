package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class PlannerResponse {
    private Boolean isOwner;
    private Integer todayCarrot;
    private List<PlanResponse> plans=new ArrayList<>();
    private List<TimerResponse> pomodoros = new ArrayList<>();
    public PlannerResponse(Planner planner, Member member){
        this.isOwner= Objects.equals(planner.getMember().getId(),member.getId());
        this.todayCarrot = planner.getCarrot();
    }
    public void addPlan(Plan plan){
        plans.add(new PlanResponse(plan));
    }
    public void addTimer(Timer timer){
        pomodoros.add(new TimerResponse(timer));
    }
}
