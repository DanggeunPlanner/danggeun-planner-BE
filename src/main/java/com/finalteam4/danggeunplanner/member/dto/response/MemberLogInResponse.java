package com.finalteam4.danggeunplanner.member.dto.response;

import lombok.Getter;

@Getter
public class MemberLogInResponse {
    private Boolean isExistUsername;

    public MemberLogInResponse(Boolean isExistUsername){
        this.isExistUsername = isExistUsername;
    }

}
