package com.finalteam4.danggeunplanner.participant.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.participant.dto.response.ParticipantInfoResponse;
import com.finalteam4.danggeunplanner.participant.service.ParticipantService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping("/{groupId}/participant")
    public ResponseEntity<ResponseMessage<ParticipantInfoResponse>> findParticipant(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ParticipantInfoResponse response = participantService.findParticipant(groupId, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹원 조회 성공", response), HttpStatus.OK);
    }

    @DeleteMapping("/{groupId}/participant")
    public ResponseEntity<ResponseMessage<Void>> deleteParticipant(@PathVariable Long groupId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        participantService.deleteParticipant(groupId, userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("그룹원 삭제 성공", null), HttpStatus.OK);
    }
}
