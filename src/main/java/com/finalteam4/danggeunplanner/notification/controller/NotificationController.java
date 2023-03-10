package com.finalteam4.danggeunplanner.notification.controller;

import com.finalteam4.danggeunplanner.common.response.ResponseMessage;
import com.finalteam4.danggeunplanner.notification.dto.response.NotificationConfirmResponse;
import com.finalteam4.danggeunplanner.notification.dto.response.NotificationReadResponse;
import com.finalteam4.danggeunplanner.notification.dto.response.NotificationResponse;
import com.finalteam4.danggeunplanner.notification.service.NotificationService;
import com.finalteam4.danggeunplanner.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    @GetMapping(value = "/api/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
                                HttpServletResponse response){

        response.setHeader("Connection", "keep-alive");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("X-Accel-Buffering", "no");

        return notificationService.subscribe(userDetails.getMember(), lastEventId);
    }

    @GetMapping("/api/member/notification")
    public ResponseEntity<ResponseMessage<List<NotificationResponse>>> findNotificationList(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<NotificationResponse> response = notificationService.findNotificationList(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("?????? ?????? ?????? ??????", response), HttpStatus.OK);
    }

    @DeleteMapping("/api/member/notification/{notificationId}")
    public ResponseEntity<ResponseMessage<NotificationConfirmResponse>> confirmNotification(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long notificationId){
        NotificationConfirmResponse response = notificationService.confirmNotification(userDetails.getMember(), notificationId);
        return new ResponseEntity<>(new ResponseMessage<>("?????? ??????", response), HttpStatus.OK);
    }

    @PatchMapping("/api/member/notification/read")
    public ResponseEntity<ResponseMessage<Void>> readNotification(@AuthenticationPrincipal UserDetailsImpl userDetails){
        notificationService.readNotification(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("?????? ??????", null), HttpStatus.OK);
    }

    @GetMapping("/api/member/notification/read")
    public ResponseEntity<ResponseMessage<NotificationReadResponse>> readFindNotification(@AuthenticationPrincipal UserDetailsImpl userDetails){
        NotificationReadResponse response = notificationService.readFindNotification(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("?????? ??????", response), HttpStatus.OK);
    }

    @GetMapping("/api/member/notification/count")
    public ResponseEntity<ResponseMessage<Long>> countUnReadNotification(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long count = notificationService.countUnReadNotification(userDetails.getMember());
        return new ResponseEntity<>(new ResponseMessage<>("?????? ?????? ?????? ??????", count), HttpStatus.OK);
    }
}
