package com.finalteam4.danggeunplanner.participant.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.participant.dto.response.ParticipantInfoResponse;
import com.finalteam4.danggeunplanner.participant.dto.response.ParticipantListResponse;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import com.finalteam4.danggeunplanner.participant.repository.ParticipantRepository;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PARTICIPANT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;
    private final TimerRepository timerRepository;
    private final ParticipantValidator participantValidator;

    public ParticipantInfoResponse findParticipant(Long groupId, Member member) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        String onlineParticipant = "5/"+group.getParticipants().size();

        boolean isAdmin = group.getAdmin().equals(member.getUsername());
        ParticipantInfoResponse response = new ParticipantInfoResponse(group, onlineParticipant, isAdmin);

        ParticipantListResponse myInfoListResponse = appendListResponse(member);
        response.addMyInfo(myInfoListResponse);

        for (Participant participant : group.getParticipants()) {
            boolean identification = member.getId().equals(participant.getMember().getId());
            if (!identification) {
                ParticipantListResponse participantListResponse = appendListResponse(participant.getMember());
                response.addParticipantList(participantListResponse);
            }
        }
        return response;
    }
    private ParticipantListResponse appendListResponse(Member member) {
        List<Timer> timers = timerRepository.findAllByMemberAndIsFinish(member,true);
        Integer dailyCarrot = timers.size();
        return new ParticipantListResponse(member,true, dailyCarrot);
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
