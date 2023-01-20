package com.finalteam4.danggeunplanner.group.dto.request;

import com.finalteam4.danggeunplanner.common.exception.ValidationGroups;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.GroupImageEnum;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

import javax.validation.constraints.Size;

@Getter
public class GroupInfoRequest {
    @Size(min = 1, max = 10, message = "그룹 이름은 1글자 이상 10글자 이하입니다.", groups = ValidationGroups.FirstSizeGroup.class)
    private String groupName;
    @Size(min = 1, max = 50, message = "그룹 설명은 1글자 이상 50글자 이하입니다.", groups = ValidationGroups.SecondSizeGroup.class)
    private String description;

    public Group toEntity(Member member, GroupImageEnum groupImageEnum){
        return Group.builder()
                .name(groupName)
                .admin(member.getUsername())
                .description(description)
                .image(groupImageEnum.getImage())
                .build();
    }
}
