package com.finalteam4.danggeunplanner.group.service;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.dto.request.GroupInfoRequest;
import com.finalteam4.danggeunplanner.group.dto.response.GroupDetailResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInfoResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupInvitationResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupListResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupSearchMemberResponse;
import com.finalteam4.danggeunplanner.group.dto.response.GroupSearchResponse;
import com.finalteam4.danggeunplanner.group.dto.response.ParticipantRankingResponse;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.entity.GroupImageEnum;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import com.finalteam4.danggeunplanner.participant.repository.ParticipantRepository;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
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
    private final TimerRepository timerRepository;
    private final MemberRepository memberRepository;
    private final GroupValidator groupValidator;

    @Transactional
    public GroupInfoResponse createGroup(GroupInfoRequest request, Member member) {
        GroupImageEnum groupImages = GroupImageEnum.pickRandomImage();
        Group group = request.toEntity(member, groupImages);
        groupRepository.save(group);

        createParticipant(member, group);

        return new GroupInfoResponse(group);
    }

    private void createParticipant(Member member, Group group) {
        Participant participant = new Participant(member, group);
        participantRepository.save(participant);
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

        Integer groupDailyCarrot = 0;
        Integer groupCarrot = 0;

        for (Participant participant : group.getParticipants()) {
            List<Timer> timers = timerRepository.findAllByMemberAndIsFinish(participant.getMember(),true);
            for(Timer timer : timers){
                groupDailyCarrot += timer.getCount();
            }
        }
        for (Calendar calendar : calendarList) {
            groupCarrot += calendar.getCarrot();
        }

        GroupDetailResponse response = new GroupDetailResponse(group, groupDailyCarrot, groupCarrot);
        bringParticipantRanking(calendarList, response);

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

    private void bringParticipantRanking(List<Calendar> calendarList, GroupDetailResponse response) {
        Integer rank = 0;
        Iterator<Calendar> calendarIter = calendarList.iterator();
        while(calendarIter.hasNext()){
            rank++;
            Calendar calendarIt = calendarIter.next();
            ParticipantRankingResponse ranking = new ParticipantRankingResponse(calendarIt);
            response.addRanking(ranking);
            if (rank >= 3) {
                break;
            }
        }
    }

    public GroupSearchResponse searchMember(Member member, Long groupId, String username) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        groupValidator.validateAdmin(member, group);

        GroupSearchResponse response = new GroupSearchResponse(group);

        List<Member> members = memberRepository.findByUsernameStartsWithOrderByUsername(username);
        for (Member other : members) {
            if (other.getId().equals(member.getId())){
                continue;
            }
            Optional<Participant> participant = participantRepository.findByMemberAndGroup(other, group);
            boolean isMember = participant.isPresent();
            GroupSearchMemberResponse invitationSearch = new GroupSearchMemberResponse(other, isMember);
            response.addMembers(invitationSearch);
        }
        return response;
    }

    @Transactional
    public GroupInvitationResponse invite(Member member, Long groupId, List<String> username) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        groupValidator.validateAdmin(member, group);

        GroupInvitationResponse response = new GroupInvitationResponse(group);
        List<Participant> participants = new ArrayList<>();

        List<Member> members = memberRepository.findAll();
        for (Member other : members) {
            for (String name : username) {
                if (participantRepository.existsByMemberAndGroup(other, group)){
                    continue;
                }
                if (other.getUsername().equals(name)){
                    Participant participant = new Participant(other, group);
                    participants.add(participant);
                    response.addUsername(participant.getMember());
                }
            }
        }
        participantRepository.saveAll(participants);
        return response;
    }
}
