package com.finalteam4.danggeunplanner.notification.dto.response;

import com.finalteam4.danggeunplanner.notification.entity.Notification;
import lombok.Getter;

@Getter
public class NotificationConfirmResponse {
    private Long notificationId;

    public NotificationConfirmResponse(Notification notification) {
        this.notificationId = notification.getId();
    }
}
