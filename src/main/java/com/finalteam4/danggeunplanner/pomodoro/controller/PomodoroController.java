package com.finalteam4.danggeunplanner.pomodoro.controller;

import com.finalteam4.danggeunplanner.pomodoro.service.PomodoroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pomodoro")
public class PomodoroController {
    private final PomodoroService pomodoroService;
}
