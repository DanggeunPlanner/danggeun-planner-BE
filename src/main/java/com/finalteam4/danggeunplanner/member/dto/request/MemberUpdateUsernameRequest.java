package com.finalteam4.danggeunplanner.member.dto.request;


import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class MemberUpdateUsernameRequest {
    @Size(min = 1, max = 6)
    String username;
}
