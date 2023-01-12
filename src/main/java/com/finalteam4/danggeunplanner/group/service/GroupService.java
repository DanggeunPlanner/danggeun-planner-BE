package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.dto.request.GroupInfoRequest;
import com.finalteam4.danggeunplanner.group.dto.response.GroupDetailMonthlyRankingResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupDetailResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInfoResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupListResponse;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.GroupImageEnum;
import com.finalteam4.danggeunplanner.group.entity.Participant;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.group.repository.ParticipantRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_JOIN_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final CalendarRepository calendarRepository;

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

        group.update(request.getGroupName(), request.getDescription());
        return new GroupInfoResponse(group);
    }

    @Transactional
    public GroupInfoResponse deleteGroup(Long groupId, Member member) {
        Group group = validateExistGroup(groupId);

        validateAccess(member, group);

        participantRepository.deleteAllByGroupId(groupId);
        groupRepository.deleteById(groupId);
        return new GroupInfoResponse(group);
    }

    public List<GroupListResponse> findGroupList(Member member) {
        List<Participant> participantList = participantRepository.findAllByMember(member);
        isExistParticipant(participantList);

        List<GroupListResponse> groupListResponse = new ArrayList<>();
        for (Participant participant : participantList) {
            Integer participants = participantRepository.countParticipantByGroup_Id(participant.getGroup().getId());
            groupListResponse.add(new GroupListResponse(participant, participants));
        }
        return groupListResponse;
    }

    public GroupDetailResponse findGroup(Long groupId, Member member) {
        Group group = validateExistGroup(groupId);
        List<Participant> participantList = participantRepository.findAllByMember(member);
        isExistParticipant(participantList);

        Integer monthlyCarrot = 0;

        List<Calendar> calendarList = new ArrayList<>();
        for (Participant participant : group.getParticipants()) {
            Member other = participant.getMember();
            Optional<Calendar> calendar = calendarRepository.findByMemberAndDate(other, TimeConverter.getCurrentTimeToYearMonth());
            if (calendar.isPresent()) {
                calendarList.add(calendar.get());
            } else {
                new Calendar(other);
            }
        }

        Comparator<Calendar> comparator = (c1, c2) -> c2.getCarrot() - c1.getCarrot();
        calendarList.sort(comparator);

        List<GroupDetailMonthlyRankingResponse> monthlyRanking = new ArrayList<>();
        Iterator<Calendar> calendarIter = calendarList.iterator();
        Integer rank = 0;
        while(calendarIter.hasNext()){
            rank++;
            Calendar calendarIt = calendarIter.next();
            monthlyRanking.add(new GroupDetailMonthlyRankingResponse(rank, calendarIt));
            if (rank >= 3) {
                break;
            }
        }
        for (Calendar calendar : calendarList) {
            monthlyCarrot += calendar.getCarrot();
        }
        return new GroupDetailResponse(group, monthlyCarrot, monthlyRanking);
    }

    private Group validateExistGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
    }

    private void validateAccess(Member member, Group group) {
        if (!group.getAdmin().equals(member.getUsername())) {
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }

    private static void isExistParticipant(List<Participant> participantList) {
        if (participantList.isEmpty()) {
            throw new DanggeunPlannerException(NOT_FOUND_JOIN_GROUP);
        }
    }
}
