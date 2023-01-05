package com.finalteam4.danggeunplanner.plan.dto.request;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.plan.entity.Plan;
import lombok.Getter;

@Getter
public class PlanInfoRequest {
    private String date;
    private String startTime;
    private String endTime;
    private String content;
    public Plan toEntity(Member member){
        return Plan.builder()
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .content(content)
                .member(member)
                .build();
    }
}
