package com.finalteam4.danggeunplanner.notification.dto.response;

import lombok.Getter;

@Getter
public class NotificationReadResponse {
    private Boolean isRead;

    public NotificationReadResponse(Boolean isRead) {
        this.isRead = isRead;
    }
}
