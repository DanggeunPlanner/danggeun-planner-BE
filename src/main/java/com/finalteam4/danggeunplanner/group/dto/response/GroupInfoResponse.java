package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

@Getter
public class GroupInfoResponse {
    private Long groupId;
    private String groupName;
    private String groupImage;
    private String groupDescription;

    public GroupInfoResponse(Group group) {
        this.groupId = group.getId();
        this.groupName = group.getGroupName();
        this.groupImage = group.getGroupImage();
        this.groupDescription = group.getDescription();
    }
}
