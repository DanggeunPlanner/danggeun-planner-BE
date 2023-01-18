package com.finalteam4.danggeunplanner.planner.dto.request;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
public class PlanRequest {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @NotNull
    @Size(max=50, message="계획의 내용은 최대 50자입니다.")
    private String content;
    public Plan toPlan(Member member){
        return Plan.builder()
                .date(TimeConverter.convertToPlannerDateForm(startTime))
                .startTime(startTime)
                .endTime(endTime)
                .content(content)
                .member(member)
                .build();
    }
}
