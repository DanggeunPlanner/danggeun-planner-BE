package com.finalteam4.danggeunplanner.member.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberMyPageResponse {
    private String email;
    private String username;
    private String profileImage;
    private Integer totalCarrot;
    private Boolean isPlannerOpened;

    public MemberMyPageResponse(Member member, Integer totalCarrot ){
        this.email = member.getEmail();
        this.username = member.getUsername();
        this.profileImage = member.getProfileImage();
        this.isPlannerOpened = member.getIsPlannerOpened();
        this.totalCarrot = totalCarrot;
    }
}
