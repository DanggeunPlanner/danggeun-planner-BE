package com.finalteam4.danggeunplanner.calendar.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class CalendarResponse {
    private String username;
    private String profileImage;
    private Double monthlyAverageCarrot;
    private String[] stage1;
    private String[] stage2;
    private String[] stage3;
    private String[] stage4;

    public CalendarResponse(Member member, Double monthlyAverageCarrot, String[] stage1, String[] stage2, String[] stage3, String[] stage4){
        this.username = member.getUsername();
        this.profileImage = member.getProfileImage();
        this.monthlyAverageCarrot = monthlyAverageCarrot;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.stage3 = stage3;
        this.stage4 = stage4;
    }
}
