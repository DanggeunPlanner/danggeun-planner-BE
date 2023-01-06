package com.finalteam4.danggeunplanner.timer.dto.response;

import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

@Getter
public class TimerResponse {
    private Long timerId;
    private String startTime;
    private String endTime;

    public TimerResponse(Timer timer){
        this.timerId = timer.getId();
        this.startTime = timer.getStartTime();
        this.endTime = timer.getEndTime();
    }
}
