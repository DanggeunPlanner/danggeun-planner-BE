package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import lombok.Getter;

@Getter
public class ParticipantRankingResponse {
    private String profileImage;
    private String username;
    private Integer carrot;

    public ParticipantRankingResponse(Calendar calendar) {
        this.profileImage = calendar.getMember().getProfileImage();
        this.username = calendar.getMember().getUsername();
        this.carrot = calendar.getCarrot();
    }
}
