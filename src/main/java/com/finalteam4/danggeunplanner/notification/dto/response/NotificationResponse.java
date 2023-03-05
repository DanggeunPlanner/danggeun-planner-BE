package com.finalteam4.danggeunplanner.notification.dto.response;

import com.finalteam4.danggeunplanner.notification.entity.Notification;
import com.finalteam4.danggeunplanner.notification.entity.NotificationType;
import lombok.Getter;

@Getter
public class NotificationResponse {
    private Long notificationId;
    private Long groupId;
    private NotificationType notificationType;
    private String content;

    public NotificationResponse(Notification notification) {
        this.notificationId = notification.getId();
        this.groupId = notification.getGroupId();
        this.notificationType = notification.getNotificationType();
        this.content = notification.getContent();
    }
}
