package com.finalteam4.danggeunplanner.planner.dto.request;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
public class PlanRequest {
    @NotNull(message = "시작 시간은 Null이 될 수 없습니다.", groups = ValidationGroups.FirstNotNullGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @NotNull(message = "종료 시간은 Null이 될 수 없습니다.", groups = ValidationGroups.SecondNotNullGroup.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    @NotNull(message = "계획의 내용은 Null이 될 수 없습니다.", groups = ValidationGroups.ThirdNotNullGroup.class)
    @Size(max=50, message="계획의 내용은 최대 50자입니다.", groups = ValidationGroups.FirstSizeGroup.class)
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
