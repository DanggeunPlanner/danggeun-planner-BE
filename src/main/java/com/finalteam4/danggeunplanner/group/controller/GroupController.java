package com.finalteam4.danggeunplanner.group.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.group.dto.request.GroupInfoRequest;
import com.finalteam4.danggeunplanner.group.dto.request.GroupInvitationRequest;
import com.finalteam4.danggeunplanner.group.dto.response.GroupDetailResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInfoResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInvitationResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupListResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupSearchResponse;
import com.finalteam4.danggeunplanner.group.service.GroupService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<ResponseMessage<GroupInfoResponse>> createGroup(@Valid @RequestBody GroupInfoRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GroupInfoResponse response = groupService.createGroup(request, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 등록 성공", response), HttpStatus.CREATED);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<ResponseMessage<GroupInfoResponse>> updateGroup(@PathVariable Long groupId, @RequestBody GroupInfoRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GroupInfoResponse response = groupService.updateGroup(groupId, request, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 수정 성공", response), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ResponseMessage<GroupInfoResponse>> deleteGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GroupInfoResponse response = groupService.deleteGroup(groupId, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 삭제 성공", response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage<List<GroupListResponse>>> findGroupList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<GroupListResponse> response = groupService.findGroupList(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 리스트 조회 성공", response), HttpStatus.OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseMessage<GroupDetailResponse>> findGroup(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        GroupDetailResponse response = groupService.findGroup(groupId, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹 조회 성공", response), HttpStatus.OK);
    }

    @GetMapping("/search/{groupId}/{username}")
    public ResponseEntity<ResponseMessage<GroupSearchResponse>> searchMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long groupId, @PathVariable String username){
        GroupSearchResponse response = groupService.searchMember(userDetails.getMember(), groupId, username);
        return new ResponseEntity<>(new ResponseMessage<>("회원 검색 성공", response), HttpStatus.OK);
    }

    @PostMapping("/invitation/{groupId}")
    public ResponseEntity<ResponseMessage<GroupInvitationResponse>> invite(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long groupId, @RequestBody GroupInvitationRequest request){
        List<String> username = request.getUsername();
        GroupInvitationResponse response = groupService.invite(userDetails.getMember(), groupId, username);
        return new ResponseEntity<>(new ResponseMessage<>("회원 초대 성공", response), HttpStatus.CREATED);
    }
}
