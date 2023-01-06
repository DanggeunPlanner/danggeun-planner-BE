package com.finalteam4.danggeunplanner.plan.dto.response;

import com.finalteam4.danggeunplanner.plan.entity.Plan;
import lombok.Getter;

@Getter
public class PlanInfoResponse {
    private final Long planId;
    private final String date;
    private final String startTime;
    private final String endTime;
    private final String content;

    public PlanInfoResponse (Plan plan){
        this.planId = plan.getId();
        this.date = plan.getDate();
        this.startTime = plan.getStartTime();
        this.endTime = plan.getEndTime();
        this.content = plan.getContent();
    }
}
