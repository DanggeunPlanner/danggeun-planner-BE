package com.finalteam4.danggeunplanner.member.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;
@Getter
public class MemberLogoutResponse {

    private String username;
    public MemberLogoutResponse(Member member){
        this.username = member.getUsername();
    }

    }

