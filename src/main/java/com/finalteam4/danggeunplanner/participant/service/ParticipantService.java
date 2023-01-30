package com.finalteam4.danggeunplanner.participant.service;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.participant.dto.response.ParticipantInfoResponse;
import com.finalteam4.danggeunplanner.participant.dto.response.ParticipantListResponse;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import com.finalteam4.danggeunplanner.participant.repository.ParticipantRepository;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PARTICIPANT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;
    private final PlannerRepository plannerRepository;
    private final ParticipantValidator participantValidator;
    private final RedisService redisService;

    public ParticipantInfoResponse findParticipant(Long groupId, Member member) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        participantValidator.validateAccess(member, group);
        Integer onlineCount = 0;
        onlineCount = onlineCalculator(group, onlineCount);

        String onlineParticipant = onlineCount + "/" + group.getParticipants().size();
        boolean isAdmin = group.getAdmin().equals(member.getUsername());
        ParticipantInfoResponse response = new ParticipantInfoResponse(group, onlineParticipant, isAdmin);

        ParticipantListResponse myInfoListResponse = appendListResponse(member, true);
        response.addMyInfo(myInfoListResponse);

        appendParticipantList(member, group, response);
        return response;
    }
    private Integer onlineCalculator(Group group, Integer onlineCount) {
        for (Participant participant : group.getParticipants()) {
            boolean online = hasRefreshToken(participant);
            if (online) {
                onlineCount++;
            }
        }
        return onlineCount;
    }
    private ParticipantListResponse appendListResponse(Member member, boolean online) {
        Integer dailyCarrot = 0;
        List<Planner> planners = plannerRepository.findAllByMemberAndDate(member, TimeConverter.convertToPlannerDateForm(LocalDateTime.now()));
        for (Planner planner : planners) {
            dailyCarrot += planner.getCarrot();
        }
        return new ParticipantListResponse(member, online, dailyCarrot);
    }
    private void appendParticipantList(Member member, Group group, ParticipantInfoResponse response) {
        for (Participant participant : group.getParticipants()) {
            boolean identification = member.getId().equals(participant.getMember().getId());
            if (!identification) {
                boolean online = hasRefreshToken(participant);
                ParticipantListResponse participantListResponse = appendListResponse(participant.getMember(), online);
                response.addParticipantList(participantListResponse);
            }
        }
        response.sortByOnline();
    }
    private boolean hasRefreshToken(Participant participant) {
        boolean online = true;
        String onlineCheck = redisService.getValues(participant.getMember().getEmail());
        if(onlineCheck == null){
            online = false;
        }
        return online;
    }
    @Transactional
    public void deleteParticipant(Long groupId, Member member) {
        Participant participant = participantRepository.findByGroup_IdAndMember(groupId, member).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PARTICIPANT)
        );
        participantValidator.validateAdmin(member, participant);
        participantRepository.deleteById(participant.getId());
    }
}

