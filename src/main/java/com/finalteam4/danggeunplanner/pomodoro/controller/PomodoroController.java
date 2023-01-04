package com.finalteam4.danggeunplanner.pomodoro.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.pomodoro.service.PomodoroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pomodoro")
public class PomodoroController {
    private final PomodoroService pomodoroService;
    @PostMapping("/{id}")
    ResponseEntity<?> finish(@PathVariable Long id){
        pomodoroService.finish(id);
        return new ResponseEntity<>(new ResponseMessage<>("뽀모도로 완료",null), HttpStatus.CREATED);
    }


}
