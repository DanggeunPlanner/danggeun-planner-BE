package com.finalteam4.danggeunplanner.member.dto.request;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberAuthRequest {

        private String email;
        private String password;

        public Member toEntity(String password){
                return Member.builder()
                        .email(email)
                        .password(password)
                        .profileImage("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/77e5ffbb-37aa-499b-b79d-9e403c43268dprofile_pic.png")
                        .build();
        }

}
