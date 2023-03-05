package com.finalteam4.danggeunplanner.calendar.controller;

import com.finalteam4.danggeunplanner.calendar.dto.response.CalendarResponse;
import com.finalteam4.danggeunplanner.calendar.service.CalendarService;
import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/{username}/{date}")
    public ResponseEntity<ResponseMessage<CalendarResponse>> find(@PathVariable String username, @PathVariable String date){
        CalendarResponse response = calendarService.find(username, date);
        return new ResponseEntity<>(new ResponseMessage<>("캘린더 조회 성공",response), HttpStatus.OK);
    }
}
