package com.finalteam4.danggeunplanner.member.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberProfileImageResponse {
    private String profileImage;
    public MemberProfileImageResponse(Member member){
        this.profileImage = member.getProfileImage();
    }
}
