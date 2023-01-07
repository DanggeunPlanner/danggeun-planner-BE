package com.finalteam4.danggeunplanner.group.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.group.dto.request.GroupInfoRequest;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInfoResponse;
import com.finalteam4.danggeunplanner.group.service.GroupService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<ResponseMessage<?>> createGroup(@RequestBody GroupInfoRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GroupInfoResponse response = groupService.createGroup(request, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 등록 성공", response), HttpStatus.CREATED);
    }
}
