package com.finalteam4.danggeunplanner.group.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupInvitationResponse {
    private Long groupId;
    private String groupName;
    private List<String> username = new ArrayList<>();

    public GroupInvitationResponse(Group group) {
        this.groupId = group.getId();
        this.groupName = group.getName();
    }
    public void addUsername(Member member){
        username.add(member.getUsername());
    }
}
