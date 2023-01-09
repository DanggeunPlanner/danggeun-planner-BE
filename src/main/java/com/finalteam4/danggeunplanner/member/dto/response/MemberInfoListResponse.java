package com.finalteam4.danggeunplanner.member.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberInfoListResponse {
    private List<MemberInfoResponse> members = new ArrayList<>();

    public void add(MemberInfoResponse memberInfoResponse){
        members.add(memberInfoResponse);
    }
}
