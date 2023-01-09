package com.finalteam4.danggeunplanner.storage.dto;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class ProfileImageResponse {
    private String profileImage;
    public ProfileImageResponse(Member member){
        this.profileImage = member.getProfileImage();
    }
}
