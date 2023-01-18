package com.finalteam4.danggeunplanner.timer.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class TimerUpdateRequest {
    @NotNull
    @Size(max=50, message="내용은 최대 50자입니다.")
    private String content;
}
