package com.finalteam4.danggeunplanner.group.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.group.dto.response.GroupJoinResponse;
import com.finalteam4.danggeunplanner.group.service.GroupJoinService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/notification/{groupId}/{notificationId}")
public class GroupJoinController {
    private final GroupJoinService groupJoinService;

    @PostMapping
    public ResponseEntity<ResponseMessage<GroupJoinResponse>> acceptGroup(@PathVariable Long groupId, @PathVariable Long notificationId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GroupJoinResponse response = groupJoinService.acceptGroup(groupId, notificationId, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 초대 승락", response), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage<GroupJoinResponse>> rejectGroup(@PathVariable Long groupId, @PathVariable Long notificationId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GroupJoinResponse response = groupJoinService.rejectGroup(groupId, notificationId, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 초대 거절", response), HttpStatus.OK);
    }
}
