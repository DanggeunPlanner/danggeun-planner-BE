package com.finalteam4.danggeunplanner.pomodoro.dto.response;

import com.finalteam4.danggeunplanner.pomodoro.entity.Pomodoro;
import lombok.Getter;

@Getter
public class PomodoroInfoResponse {
    private Long pomodoroId;
    private String startTime;
    private String endTime;

    public PomodoroInfoResponse (Pomodoro pomodoro){
        this.pomodoroId = pomodoro.getId();
        this.startTime = pomodoro.getStartTime();
        this.endTime = pomodoro.getEndTime();
    }
}
