package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import lombok.Getter;

@Getter
public class GroupDetailMonthlyRankingResponse {
    private Integer rank;
    private String username;
    private Integer monthlyCarrot;

    public GroupDetailMonthlyRankingResponse(Integer rank, Calendar calendar) {
        this.rank = rank;
        this.username = calendar.getMember().getUsername();
        this.monthlyCarrot = calendar.getCarrot();
    }
}
