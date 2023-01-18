package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupSearchResponse {
    private Long groupId;
    private String groupName;
    private List<GroupSearchMemberResponse> members = new ArrayList<>();

    public GroupSearchResponse(Group group) {
        this.groupId = group.getId();
        this.groupName = group.getName();
    }
    public void addMembers(GroupSearchMemberResponse searchResponse){
        members.add(searchResponse);
    }
}
