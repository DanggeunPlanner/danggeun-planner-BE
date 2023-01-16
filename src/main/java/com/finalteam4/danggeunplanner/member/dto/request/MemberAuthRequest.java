package com.finalteam4.danggeunplanner.member.dto.request;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Getter
public class MemberAuthRequest {

        @Email(regexp = "/^([\\w\\.\\_\\-])*[a-zA-Z0-9]+([\\w\\.\\_\\-])*([a-zA-Z0-9])+([\\w\\.\\_\\-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,8}$/")
        private String email;

        @Pattern(regexp = "/^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\\\(\\\\)\\-_=+]).{8,13}$/")
        private String password;

        public Member toEntity(String password){
                return Member.builder()
                        .email(email)
                        .password(password)
                        .profileImage("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/77e5ffbb-37aa-499b-b79d-9e403c43268dprofile_pic.png")
                        .build();
        }

}
