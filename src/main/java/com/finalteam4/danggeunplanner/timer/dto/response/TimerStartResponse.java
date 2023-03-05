package com.finalteam4.danggeunplanner.timer.dto.response;

import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

@Getter
public class TimerStartResponse {
    private final Long timerId;

    public TimerStartResponse(Timer timer){
        this.timerId = timer.getId();
    }
}
