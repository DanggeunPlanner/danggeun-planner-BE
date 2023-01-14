package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

@Getter
public class GroupInfoResponse {
    private Long groupId;
    private String groupName;
    private String groupImage;
    private String description;

    public GroupInfoResponse(Group group) {
        this.groupId = group.getId();
        this.groupName = group.getName();
        this.groupImage = group.getImage();
        this.description = group.getDescription();
    }
}
