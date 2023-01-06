package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.planner.entity.Plan;
import lombok.Getter;

@Getter
public class PlanResponse {
    private final Long planId;

    private final String startTime;
    private final String endTime;
    private final String content;

    public PlanResponse(Plan plan){
        this.planId = plan.getId();
        this.startTime = plan.getStartTime();
        this.endTime = plan.getEndTime();
        this.content = plan.getContent();
    }
}
