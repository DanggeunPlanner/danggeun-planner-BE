package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.Participant;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_JOIN_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupValidator {
    public void validateAdmin(Member member, Group group) {
        if (!group.getAdmin().equals(member.getUsername())) {
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
    public void validateJoinGroup(List<Participant> participantList) {
        if (participantList.isEmpty()) {
            throw new DanggeunPlannerException(NOT_FOUND_JOIN_GROUP);
        }
    }
}
