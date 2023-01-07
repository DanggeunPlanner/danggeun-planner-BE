package com.finalteam4.danggeunplanner.group.dto.request;

import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.GroupImageEnum;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

@Getter
public class GroupInfoRequest {
    private String groupName;
    private String description;

    public Group toEntity(Member member, GroupImageEnum groupImageEnum){
        return Group.builder()
                .groupName(groupName)
                .member(member)
                .description(description)
                .groupImage(groupImageEnum.getImage())
                .build();
    }
}
