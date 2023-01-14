package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Participant;
import lombok.Getter;

@Getter
public class GroupListResponse {
    private Long groupId;
    private String groupName;
    private String groupImage;
    private Integer participants;

    public GroupListResponse(Participant participant, Integer participants) {
        this.groupId = participant.getGroup().getId();
        this.groupName = participant.getGroup().getName();
        this.groupImage = participant.getGroup().getImage();
        this.participants = participants;
    }
}
