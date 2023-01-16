package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupDetailResponse {
    private Long groupId;
    private String groupImage;
    private String groupName;
    private String description;
    private Integer groupDailyCarrot;
    private Integer groupCarrot;
    private List<ParticipantRankingResponse> ranking = new ArrayList<>();

    public GroupDetailResponse(Group group, Integer groupDailyCarrot, Integer groupCarrot) {
        this.groupId = group.getId();
        this.groupImage = group.getImage();
        this.groupName = group.getName();
        this.description = group.getDescription();
        this.groupDailyCarrot = groupDailyCarrot;
        this.groupCarrot = groupCarrot;
    }
    public void addRanking(ParticipantRankingResponse rankingResponse){
        ranking.add(rankingResponse);
    }
}
