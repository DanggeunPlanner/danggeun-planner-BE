package com.finalteam4.danggeunplanner.timer.dto.request;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class TimerStartRequest {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    public Timer toTimer(Member member){
        return Timer.builder()
                .member(member)
                .startTime(startTime)
                .content("당근 수확!\uD83D\uDE0A")
                .count(0)
                .isFinish(false)
                .build();
    }
}
