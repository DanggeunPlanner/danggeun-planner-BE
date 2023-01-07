package com.finalteam4.danggeunplanner.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoResponse {

    private Long memberId;
    private String username;
    private String profileImage;

}
