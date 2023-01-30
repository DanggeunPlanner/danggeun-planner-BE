package com.finalteam4.danggeunplanner.notification.entity;

public enum NotificationType {
    INVITATION(" 그룹 신청이 도착했습니다."),
    ACCEPT(" 그룹 신청이 승인되었습니다."),
    REJECT(" 그룹 신청이 거절되었습니다.");

    private String content;

    NotificationType(String content) {
        this.content = content;
    }
}
