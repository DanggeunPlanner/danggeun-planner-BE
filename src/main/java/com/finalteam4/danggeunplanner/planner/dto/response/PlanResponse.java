package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.planner.entity.Plan;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PlanResponse {
    private final Long planId;
    private final String date;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final String content;

    public PlanResponse(Plan plan){
        this.planId = plan.getId();
        this.date = plan.getDate();
        this.startTime = plan.getStartTime();
        this.endTime = plan.getEndTime();
        this.content = plan.getContent();
    }
}
