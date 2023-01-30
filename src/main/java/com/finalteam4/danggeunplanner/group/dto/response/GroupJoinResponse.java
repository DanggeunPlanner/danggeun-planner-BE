package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

@Getter
public class GroupJoinResponse {
    private Long groupId;

    public GroupJoinResponse(Group group) {
        this.groupId = group.getId();
    }
}
