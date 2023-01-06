package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlannerResponse {
    private boolean owner;
    private Integer todayCarrot;
    private List<PlanResponse> plans=new ArrayList<>();
    private List<TimerResponse> pomodoros = new ArrayList<>();
    public PlannerResponse(boolean owner,Integer todayCarrot){
        this.owner=owner;
        this.todayCarrot=todayCarrot;
    }
    public void addPlan(Plan plan){
        plans.add(new PlanResponse(plan));
    }
    public void addPomodoro(Timer timer){
        pomodoros.add(new TimerResponse(timer));
    }
}
