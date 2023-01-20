package com.finalteam4.danggeunplanner.timer.controller;

import com.finalteam4.danggeunplanner.common.exception.ValidationSequence;
import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerFinishRequest;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerStartRequest;
import com.finalteam4.danggeunplanner.timer.dto.request.TimerUpdateRequest;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerStartResponse;
import com.finalteam4.danggeunplanner.timer.service.TimerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timer")
public class TimerController {
    private final TimerService timerService;
    @PostMapping
    ResponseEntity<ResponseMessage<TimerStartResponse>> start(@AuthenticationPrincipal UserDetailsImpl userDetails, @Validated(ValidationSequence.class) @RequestBody TimerStartRequest request ){
        TimerStartResponse response = timerService.start(userDetails.getMember(), request);
        return new ResponseEntity<>(new ResponseMessage<>("타이머 시작", response),HttpStatus.CREATED);
    }
    @PutMapping("/{timerId}")
    ResponseEntity<ResponseMessage<TimerResponse>> finish(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long timerId, @Validated(ValidationSequence.class) @RequestBody TimerFinishRequest request){
        TimerResponse response = timerService.finish(userDetails.getMember(), timerId, request);
        return new ResponseEntity<>(new ResponseMessage<>("타이머 완료",response), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{timerId}/content")
    ResponseEntity<ResponseMessage<TimerResponse>> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long timerId, @Validated(ValidationSequence.class) @RequestBody TimerUpdateRequest request){
        TimerResponse response = timerService.update(userDetails.getMember(), timerId, request);
        return new ResponseEntity<>(new ResponseMessage<>("타이머 내용 변경",response), HttpStatus.ACCEPTED);
    }

}
