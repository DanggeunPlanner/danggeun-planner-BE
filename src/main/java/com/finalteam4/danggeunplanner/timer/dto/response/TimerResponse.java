package com.finalteam4.danggeunplanner.timer.dto.response;

import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

@Getter
public class TimerResponse {
    private final Long timerId;
    private final String date;
    private final String startTime;
    private final String endTime;

    public TimerResponse(Timer timer){
        this.timerId = timer.getId();
        this.date = timer.getDate();
        this.startTime = timer.getStartTime();
        this.endTime = timer.getEndTime();
    }
}
