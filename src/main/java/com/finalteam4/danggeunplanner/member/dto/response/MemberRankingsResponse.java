package com.finalteam4.danggeunplanner.member.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberRankingsResponse {
    private final List<MemberRanking> memberRankings = new ArrayList<>();

    public void addMemberRanking(MemberRanking memberRanking) {
        memberRankings.add(memberRanking);
    }

}
