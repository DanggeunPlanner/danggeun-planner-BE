package com.finalteam4.danggeunplanner.member.dto.request;

import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class OauthLoginRequest {
    private String email;

    public OauthLoginRequest(String email){
        this.email = email;
    }

    public Member toEntity(String email){
        return Member.builder()
                .email(email)
                .profileImage("https://danggeunplanner-bucket.s3.ap-northeast-2.amazonaws.com/images/20fe6551-c1b5-4c81-aff5-f31d75739293rabbit.png")
                .isPlannerOpened(true)
                .build();
    }
}
