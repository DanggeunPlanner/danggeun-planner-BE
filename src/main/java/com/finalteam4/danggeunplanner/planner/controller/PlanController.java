package com.finalteam4.danggeunplanner.planner.controller;

import com.finalteam4.danggeunplanner.common.exception.ValidationSequence;
import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.planner.dto.request.PlanRequest;
import com.finalteam4.danggeunplanner.planner.dto.response.PlanResponse;
import com.finalteam4.danggeunplanner.planner.service.PlanService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanService planService;

    @PostMapping
    public ResponseEntity<ResponseMessage<PlanResponse>> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @Validated(ValidationSequence.class) @RequestBody PlanRequest request){
        PlanResponse response = planService.create(userDetails.getMember(), request);
        return new ResponseEntity<>(new ResponseMessage<>("계획 등록 성공", response), HttpStatus.CREATED);
    }

    @PutMapping("/{planId}")
    public ResponseEntity<ResponseMessage<PlanResponse>> update(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long planId, @Validated(ValidationSequence.class) @RequestBody PlanRequest request){
        PlanResponse response = planService.update(userDetails.getMember(), planId,request);
        return new ResponseEntity<>(new ResponseMessage<>("계획 변경 성공", response), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<ResponseMessage<PlanResponse>> delete(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long planId){
        PlanResponse response = planService.delete(userDetails.getMember(), planId);
        return new ResponseEntity<>(new ResponseMessage<>("계획 삭제 성공", response), HttpStatus.OK);
    }
}
