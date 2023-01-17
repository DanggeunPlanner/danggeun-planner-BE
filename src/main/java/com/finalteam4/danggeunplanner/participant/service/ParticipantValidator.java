package com.finalteam4.danggeunplanner.participant.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_DELETE_PARTICIPANT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantValidator {
    public void validateAdmin(Member member, Participant participant) {
        if(participant.getGroup().getAdmin().equals(member.getUsername())){
            throw new DanggeunPlannerException(NOT_DELETE_PARTICIPANT);
        }
    }
}
