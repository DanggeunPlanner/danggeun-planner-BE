package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.group.dto.request.GroupInfoRequest;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInfoResponse;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.GroupImageEnum;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;

    @Transactional
    public GroupInfoResponse createGroup(GroupInfoRequest request, Member member) {
        GroupImageEnum groupImages = GroupImageEnum.pickRandomImage();
        Group group = request.toEntity(member, groupImages);

        groupRepository.save(group);

        return new GroupInfoResponse(group);
    }
}
