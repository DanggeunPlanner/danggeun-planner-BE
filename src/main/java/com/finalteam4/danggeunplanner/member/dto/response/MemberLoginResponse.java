package com.finalteam4.danggeunplanner.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLoginResponse {
    private Boolean isExistUsername;

    public MemberLoginResponse(Boolean isExistUsername){
        this.isExistUsername = isExistUsername;
    }

}
