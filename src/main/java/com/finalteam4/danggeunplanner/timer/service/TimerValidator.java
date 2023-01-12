package com.finalteam4.danggeunplanner.timer.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.ALREADY_FINISHED_TIMER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.EXPIRED_TIMER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.IS_RUNNING_TIMER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimerValidator {
     public void validateActiveTimer(Timer timer){
        if(LocalDateTime.now().isBefore(timer.getCreatedAt().plusMinutes(24))){
            throw new DanggeunPlannerException(IS_RUNNING_TIMER);
        }
        else if(LocalDateTime.now().isAfter(timer.getCreatedAt().plusMinutes(26))){
            throw new DanggeunPlannerException(EXPIRED_TIMER);
        }
    }
    public void validateFinish(Timer timer){
        if(timer.getIsFinish()){
            throw new DanggeunPlannerException(ALREADY_FINISHED_TIMER);
        }
    }
}
