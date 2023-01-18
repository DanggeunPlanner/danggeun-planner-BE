package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class GroupSearchMemberResponse {
    private Long memberId;
    private String username;
    private String profileImage;
    private Boolean isMember;

    public GroupSearchMemberResponse(Member member, Boolean isMember) {
        this.memberId = member.getId();
        this.username = member.getUsername();
        this.profileImage = member.getProfileImage();
        this.isMember = isMember;
    }
}
