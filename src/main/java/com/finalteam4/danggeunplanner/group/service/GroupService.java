package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.dto.request.GroupInfoRequest;
import com.finalteam4.danggeunplanner.group.dto.response.GroupDetailResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInfoResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupListResponse;
import com.finalteam4.danggeunplanner.group.dto.response.ParticipantRankingResponse;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.GroupImageEnum;
import com.finalteam4.danggeunplanner.group.entity.Participant;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.group.repository.ParticipantRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final CalendarRepository calendarRepository;
    private final GroupValidator groupValidator;

    @Transactional
    public GroupInfoResponse createGroup(GroupInfoRequest request, Member member) {
        GroupImageEnum groupImages = GroupImageEnum.pickRandomImage();
        Group group = request.toEntity(member, groupImages);

        groupRepository.save(group);

        return new GroupInfoResponse(group);
    }

    @Transactional
    public GroupInfoResponse updateGroup(Long groupId, GroupInfoRequest request, Member member) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        groupValidator.validateAdmin(member, group);

        group.update(request.getGroupName(), request.getDescription());
        return new GroupInfoResponse(group);
    }

    @Transactional
    public GroupInfoResponse deleteGroup(Long groupId, Member member) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        groupValidator.validateAdmin(member, group);

        participantRepository.deleteAllByGroupId(groupId);
        groupRepository.deleteById(groupId);
        return new GroupInfoResponse(group);
    }

    public List<GroupListResponse> findGroupList(Member member) {
        List<Participant> participantList = participantRepository.findAllByMember(member);
        groupValidator.validateJoinGroup(participantList);

        List<GroupListResponse> groupListResponse = new ArrayList<>();
        for (Participant participant : participantList) {
            Integer participants = participantRepository.countParticipantByGroup_Id(participant.getGroup().getId());
            groupListResponse.add(new GroupListResponse(participant, participants));
        }
        return groupListResponse;
    }

    public GroupDetailResponse findGroup(Long groupId, Member member) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        
        List<Participant> participantList = participantRepository.findAllByMember(member);
        groupValidator.validateJoinGroup(participantList);

        List<Calendar> calendarList = new ArrayList<>();
        bringCalendarListOfParticipant(group, calendarList);

        descendingOrderByCarrot(calendarList);

        Integer groupCarrot = 0;
        Integer rank = 0;

        for (Calendar calendar : calendarList) {
            groupCarrot += calendar.getCarrot();
        }

        GroupDetailResponse response = new GroupDetailResponse(group, groupCarrot);
        bringParticipantRanking(calendarList, rank, response);

        return response;
    }

    private void bringCalendarListOfParticipant(Group group, List<Calendar> calendarList) {
        for (Participant participant : group.getParticipants()) {
            Member other = participant.getMember();
            Optional<Calendar> calendar = calendarRepository.findByMemberAndDate(other, TimeConverter.convertToCalendarDateForm(LocalDateTime.now()));
            if (calendar.isPresent()) {
                calendarList.add(calendar.get());
            } else {
                new Calendar(other, TimeConverter.convertToCalendarDateForm(LocalDateTime.now()));
            }
        }
    }

    private void descendingOrderByCarrot(List<Calendar> calendarList) {
        Comparator<Calendar> comparator = (c1, c2) -> c2.getCarrot() - c1.getCarrot();
        calendarList.sort(comparator);
    }

    private void bringParticipantRanking(List<Calendar> calendarList, Integer rank, GroupDetailResponse response) {
        Iterator<Calendar> calendarIter = calendarList.iterator();
        while(calendarIter.hasNext()){
            rank++;
            Calendar calendarIt = calendarIter.next();
            ParticipantRankingResponse ranking = new ParticipantRankingResponse(rank, calendarIt);
            response.addRanking(ranking);
            if (rank >= 3) {
                break;
            }
        }
    }
}
