package com.finalteam4.danggeunplanner.participant.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class ParticipantListResponse {
    private String username;
    private String profileImage;
    private Boolean online;
    private Integer dailyCarrot;

    public ParticipantListResponse(Member member, Boolean online, Integer dailyCarrot) {
        this.username = member.getUsername();
        this.profileImage = member.getProfileImage();
        this.online = online;
        this.dailyCarrot = dailyCarrot;
    }
}
