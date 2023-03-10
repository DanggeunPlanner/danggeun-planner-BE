package com.finalteam4.danggeunplanner.timer.dto.response;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;

@Getter
public class TimerResponse {
    private final Long timerId;
    private final String startTime;
    private final String endTime;
    private final String content;
    private final Integer count;

    public TimerResponse(Timer timer){
        this.timerId = timer.getId();
        this.startTime = TimeConverter.convertToHourMinute(timer.getStartTime());
        this.endTime = TimeConverter.convertToHourMinute(timer.getEndTime());
        this.content = timer.getContent();
        this.count = timer.getCount();
    }
}
