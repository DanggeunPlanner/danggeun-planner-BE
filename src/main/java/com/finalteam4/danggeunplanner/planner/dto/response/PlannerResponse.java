package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.pomodoro.dto.response.PomodoroInfoResponse;
import com.finalteam4.danggeunplanner.pomodoro.entity.Pomodoro;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlannerResponse {
    private boolean isOwner;
    private Integer todayCarrot;
    private List<PlanResponse> plans=new ArrayList<>();
    private List<PomodoroInfoResponse> pomodoros = new ArrayList<>();
    public PlannerResponse(boolean isOwner,Integer todayCarrot){
        this.isOwner=isOwner;
        this.todayCarrot=todayCarrot;
    }
    public void addPlan(Plan plan){
        plans.add(new PlanResponse(plan));
    }
    public void addPomodoro(Pomodoro pomodoro){
        pomodoros.add(new PomodoroInfoResponse(pomodoro));
    }
}
