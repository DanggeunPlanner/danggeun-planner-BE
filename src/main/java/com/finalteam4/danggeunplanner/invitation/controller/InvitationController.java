package com.finalteam4.danggeunplanner.invitation.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationListResponse;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationResponse;
import com.finalteam4.danggeunplanner.invitation.service.InvitationService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group/invitation")
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping("/{groupId}")
    public ResponseEntity<ResponseMessage<Void>> create(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long groupId){
        invitationService.create(userDetails.getMember(),groupId);
        return new ResponseEntity<>(new ResponseMessage<>("초대 리스트 생성 성공", null), HttpStatus.CREATED);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseMessage<InvitationListResponse>> find(@AuthenticationPrincipal UserDetailsImpl userDetails,@PathVariable Long groupId){
        InvitationListResponse response = invitationService.find(userDetails.getMember(),groupId);
        return new ResponseEntity<>(new ResponseMessage<>("초대 리스트 조회 성공", response ),HttpStatus.OK);
    }

    @PutMapping("/add/{groupId}/{username}")
    public ResponseEntity<ResponseMessage<InvitationResponse>> addMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long groupId, @PathVariable String username){
        InvitationResponse response = invitationService.addMember(userDetails.getMember(), groupId, username);
        return new ResponseEntity<>(new ResponseMessage<>("초대 리스트에 회원 추가 성공", response), HttpStatus.ACCEPTED);
    }

    @PutMapping("/remove/{groupId}/{username}")
    public ResponseEntity<ResponseMessage<InvitationResponse>> removeMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long groupId ,@PathVariable String username){
        InvitationResponse response = invitationService.removeMember(userDetails.getMember(), groupId, username);
        return new ResponseEntity<>(new ResponseMessage<>("초대 리스트에 회원 제거 성공", response), HttpStatus.ACCEPTED);
    }
}
