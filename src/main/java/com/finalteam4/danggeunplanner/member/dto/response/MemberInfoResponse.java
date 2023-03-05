package com.finalteam4.danggeunplanner.member.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberInfoResponse {

    private Long memberId;
    private String username;
    private String profileImage;

    public MemberInfoResponse(Member member){
        this.memberId = member.getId();
        this.username = member.getUsername();
        this.profileImage = member.getProfileImage();
    }

}
