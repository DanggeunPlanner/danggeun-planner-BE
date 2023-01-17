package com.finalteam4.danggeunplanner.invitation.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvitationGroupResponse {
    private Long groupId;
    private String groupName;
    private List<InvitationSearchResponse> members = new ArrayList<>();

    public InvitationGroupResponse(Group group) {
        this.groupId = group.getId();
        this.groupName = group.getName();
    }
    public void addMembers(InvitationSearchResponse searchResponse){
        members.add(searchResponse);
    }
}
