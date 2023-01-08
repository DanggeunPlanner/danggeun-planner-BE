package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.dto.request.GroupInfoRequest;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInfoResponse;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.GroupImageEnum;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

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

    @Transactional
    public GroupInfoResponse updateGroup(Long groupId, GroupInfoRequest request, Member member) {
        Group group = validateExistGroup(groupId);

        validateAccess(member, group);

        group.update(request.getGroupName(),request.getDescription());
        return new GroupInfoResponse(group);
    }

    private Group validateExistGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
    }

    private void validateAccess(Member member, Group group) {
        if(!group.getAdmin().equals(member.getUsername())){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
}
