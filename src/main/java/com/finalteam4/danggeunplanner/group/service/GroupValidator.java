package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.notification.entity.Notification;
import com.finalteam4.danggeunplanner.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.OVER_HEAD_COUNT_GROUP;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupValidator {
    private final ParticipantRepository participantRepository;
    public void validateAdmin(Member member, Group group) {
        if (!group.getAdmin().equals(member.getUsername())) {
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
    public void validateNotification(Member member, Notification notification) {
        if (!notification.getMember().getId().equals(member.getId())){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
    public void validateHeadCount(Group group) {
        if (group.getParticipants().size() >= 99){
            throw new DanggeunPlannerException(OVER_HEAD_COUNT_GROUP);
        }
    }
    public void validateAccess(Member member, Group group) {
        if (!participantRepository.existsByMemberAndGroup(member, group)){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }
}
