package com.finalteam4.danggeunplanner.timer.dto.request;

import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class TimerUpdateRequest {
    @NotNull(message = "내용은 Null이 될 수 없습니다.", groups = ValidationGroups.FirstNotNullGroup.class)
    @Size(max=50, message="내용은 최대 50자입니다.", groups = ValidationGroups.FirstSizeGroup.class)
    private String content;
}
