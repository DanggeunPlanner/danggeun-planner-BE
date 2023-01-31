package com.finalteam4.danggeunplanner.member.dto.request;

import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class MemberAuthRequest {

        @Pattern(regexp = "^([\\w\\.\\_\\-])*[a-zA-Z0-9]+([\\w\\.\\_\\-])*([a-zA-Z0-9])+([\\w\\.\\_\\-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,8}$", message = "이메일 형식이 올바르지 않습니다.", groups = ValidationGroups.EmailPatternGroup.class)
        private String email;

        @Pattern(regexp = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\\\(\\\\)\\-_=+]).{8,13}$", message = "비밀번호는 8~13자 영문, 숫자, 특수문자를 사용하세요.", groups = ValidationGroups.PasswordPatternGroup.class)
        private String password;

        public Member toEntity(String password){
                return Member.builder()
                        .email(email)
                        .password(password)
                        .profileImage("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/77e5ffbb-37aa-499b-b79d-9e403c43268dprofile_pic.png")
                        .isPlannerOpened(true)
                        .build();
        }

}
