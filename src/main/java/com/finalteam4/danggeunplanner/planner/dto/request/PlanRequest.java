package com.finalteam4.danggeunplanner.planner.dto.request;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import lombok.Getter;

@Getter
public class PlanRequest {
    private String date;
    private String startTime;
    private String endTime;
    private String content;
    public Plan toPlan(Member member){
        return Plan.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .content(content)
                .member(member)
                .build();
    }
}
