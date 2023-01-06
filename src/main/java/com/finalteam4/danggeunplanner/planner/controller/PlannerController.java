package com.finalteam4.danggeunplanner.planner.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.planner.dto.response.PlannerResponse;
import com.finalteam4.danggeunplanner.planner.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PlannerController {
    private final PlanService planService;

    @GetMapping("/planner/{memberId}/{searchId}/{date}")
    public ResponseEntity<ResponseMessage> find (@PathVariable Long memberId, @PathVariable Long searchId, @PathVariable String date){
        PlannerResponse response = planService.find(memberId,searchId, date);
        return new ResponseEntity<>(new ResponseMessage("플래너 조회 성공",response), HttpStatus.OK);
    }
}
