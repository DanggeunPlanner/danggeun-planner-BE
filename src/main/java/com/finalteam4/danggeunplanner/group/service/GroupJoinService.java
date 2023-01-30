package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.dto.response.GroupJoinResponse;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.notification.entity.Notification;
import com.finalteam4.danggeunplanner.notification.entity.NotificationType;
import com.finalteam4.danggeunplanner.notification.repository.NotificationRepository;
import com.finalteam4.danggeunplanner.notification.service.NotificationService;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import com.finalteam4.danggeunplanner.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_NOTIFICATION;

@Service
@RequiredArgsConstructor
public class GroupJoinService {
    private final GroupRepository groupRepository;
    private final NotificationRepository notificationRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final GroupValidator groupValidator;

    @Transactional
    public synchronized GroupJoinResponse acceptGroup(Long groupId, Long notificationId, Member member) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_NOTIFICATION)
        );
        Member adminMember = memberRepository.findByUsername(group.getAdmin()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        groupValidator.validateNotification(member, notification);
        groupValidator.validateHeadCount(group);

        Participant participant = new Participant(member, group);
        participantRepository.save(participant);
        notificationRepository.deleteById(notificationId);

        String content = member.getUsername() + "님이 " + group.getName() + " 그룹 초대를 승인했습니다.";
        notificationService.send(adminMember, NotificationType.ACCEPT, content, groupId);

        return new GroupJoinResponse(group);
    }

    @Transactional
    public GroupJoinResponse rejectGroup(Long groupId, Long notificationId, Member member) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_NOTIFICATION)
        );
        Member adminMember = memberRepository.findByUsername(group.getAdmin()).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );

        groupValidator.validateNotification(member, notification);

        notificationRepository.deleteById(notificationId);

        String content = member.getUsername() + "님이 " + group.getName() + " 그룹 초대를 거절했습니다.";
        notificationService.send(adminMember, NotificationType.REJECT, content, groupId);

        return new GroupJoinResponse(group);
    }
}
