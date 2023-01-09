package com.finalteam4.danggeunplanner.planner.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.service.PlannerService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planner")
public class PlannerController {
    private final PlannerService plannerService;

    @GetMapping("{username}/{date}")
    public ResponseEntity<ResponseMessage> find (@AuthenticationPrincipal final UserDetailsImpl userDetails, @PathVariable final String username, @PathVariable String date){
        PlannerResponse response = plannerService.find(userDetails.getMember(), username, date);
        return new ResponseEntity<>(new ResponseMessage("플래너 조회 성공", response), HttpStatus.OK);
    }
}
