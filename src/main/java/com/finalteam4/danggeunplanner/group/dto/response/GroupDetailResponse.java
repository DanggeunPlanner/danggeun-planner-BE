package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupDetailResponse {
    private Long groupId;
    private String groupName;
    private String description;
    private Integer monthlyCarrot;
    private List<GroupDetailMonthlyRankingResponse> monthlyRanking;

    public GroupDetailResponse(Group group, Integer monthlyCarrot, List<GroupDetailMonthlyRankingResponse> monthlyRanking) {
        this.groupId = group.getId();
        this.groupName = group.getGroupName();
        this.description = group.getDescription();
        this.monthlyCarrot = monthlyCarrot;
        this.monthlyRanking = monthlyRanking;
    }
}
