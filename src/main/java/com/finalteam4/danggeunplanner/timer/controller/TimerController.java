package com.finalteam4.danggeunplanner.timer.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import com.finalteam4.danggeunplanner.timer.service.TimerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/timer")
public class TimerController {
    private final TimerService timerService;
    @PostMapping
    ResponseEntity<ResponseMessage> finish(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        timerService.finish(member);
        return new ResponseEntity<>(new ResponseMessage<>("타이머 등록 완료",null), HttpStatus.CREATED);
    }
}
