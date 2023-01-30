package com.finalteam4.danggeunplanner.member.dto.response;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberRanking {
    private final Long memberId;
    private final String profileImage;
    private final String username;
    private final Integer carrot;
    private final Integer ranking;

    public MemberRanking(Member member, Calendar calendar, Integer ranking) {
        this.memberId = member.getId();
        this.profileImage = member.getProfileImage();
        this.username = member.getUsername();
        this.carrot = calendar.getCarrot();
        this.ranking = ranking;
    }
}
