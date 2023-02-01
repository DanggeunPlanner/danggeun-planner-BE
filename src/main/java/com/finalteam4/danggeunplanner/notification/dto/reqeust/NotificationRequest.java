package com.finalteam4.danggeunplanner.notification.dto.reqeust;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationRequest {
    private Long groupId;
    private NotificationType notificationType;
    private String content;
    private Member member;
}
