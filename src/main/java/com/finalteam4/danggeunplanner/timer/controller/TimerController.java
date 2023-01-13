package com.finalteam4.danggeunplanner.timer.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.service.TimerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timer")
public class TimerController {
    private final TimerService timerService;
    @PostMapping
    ResponseEntity<ResponseMessage<TimerResponse>> start(@AuthenticationPrincipal UserDetailsImpl userDetails){
        TimerResponse response = timerService.start(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("타이머 시작", response),HttpStatus.CREATED);
    }
    @PutMapping("/{timerId}")
    ResponseEntity<ResponseMessage<TimerResponse>> finish(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long timerId){
        TimerResponse response = timerService.finish(userDetails.getMember(), timerId);
        return new ResponseEntity<>(new ResponseMessage<>("타이머 완료",response), HttpStatus.ACCEPTED);
    }
}
