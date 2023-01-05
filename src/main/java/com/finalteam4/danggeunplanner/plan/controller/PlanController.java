package com.finalteam4.danggeunplanner.plan.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.plan.dto.request.PlanInfoRequest;
import com.finalteam4.danggeunplanner.plan.dto.response.PlanInfoResponse;
import com.finalteam4.danggeunplanner.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanService planService;
    @PostMapping("/{id}")
    public ResponseEntity<ResponseMessage> createPlan(@PathVariable Long id, @RequestBody PlanInfoRequest request){
        PlanInfoResponse response = planService.createPlan(id,request);
        return new ResponseEntity<>(new ResponseMessage("계획 등록 성공",response), HttpStatus.CREATED);
    }
}
