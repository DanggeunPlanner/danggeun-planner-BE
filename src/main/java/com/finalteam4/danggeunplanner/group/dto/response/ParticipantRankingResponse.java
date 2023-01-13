package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import lombok.Getter;

@Getter
public class ParticipantRankingResponse {
    private Integer rank;
    private String username;
    private Integer carrot;

    public ParticipantRankingResponse(Integer rank, Calendar calendar) {
        this.rank = rank;
        this.username = calendar.getMember().getUsername();
        this.carrot = calendar.getCarrot();
    }
}
