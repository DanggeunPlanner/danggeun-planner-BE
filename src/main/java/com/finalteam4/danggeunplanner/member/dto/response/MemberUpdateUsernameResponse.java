package com.finalteam4.danggeunplanner.member.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberUpdateUsernameResponse {
    private String username;
    public MemberUpdateUsernameResponse(Member member){
        this.username = member.getUsername();
    }
}
