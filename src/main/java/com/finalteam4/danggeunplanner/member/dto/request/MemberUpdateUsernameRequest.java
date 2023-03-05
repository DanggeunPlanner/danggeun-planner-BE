package com.finalteam4.danggeunplanner.member.dto.request;


import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class MemberUpdateUsernameRequest {
    @Size(min = 1, max = 6, message = "회원 이름은 1글자 이상 6글자 이하 입니다.", groups = ValidationGroups.FirstSizeGroup.class)
    String username;
}
