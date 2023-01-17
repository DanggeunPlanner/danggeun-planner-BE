package com.finalteam4.danggeunplanner.invitation.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class InvitationSearchResponse {
    private Long memberId;
    private String username;
    private String profileImage;
    private Boolean isMember;

    public InvitationSearchResponse(Member member, Boolean isMember) {
        this.memberId = member.getId();
        this.username = member.getUsername();
        this.profileImage = member.getProfileImage();
        this.isMember = isMember;
    }
}
