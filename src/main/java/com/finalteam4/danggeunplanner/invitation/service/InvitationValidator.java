package com.finalteam4.danggeunplanner.invitation.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.invitation.entity.Invitation;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.ALREADY_INVITED_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.ALREADY_PARTICIPATED_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.EXCEED_INVITATION_MAX_SIZE;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_INVITED_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvitationValidator {

    public void validateMaxSize(Invitation invitation, Group group){
        if(group.getParticipants().size() + invitation.getMembers().size()>30){
            throw new DanggeunPlannerException(EXCEED_INVITATION_MAX_SIZE);
        }
    }
    public void validateAdmin(Member member, Group group){
        if(!group.getAdmin().equals(member.getUsername())){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }

    public void validateInvitedMember(Invitation invitation, Member member){
        if(invitation.getMembers().contains(member)){
            throw new DanggeunPlannerException(ALREADY_INVITED_MEMBER);
        }
    }

    public void validateParticipant(Group group, Member member){
        for(Participant participant : group.getParticipants()){
            if(participant.getMember().getId().equals(member.getId())){
                throw new DanggeunPlannerException(ALREADY_PARTICIPATED_MEMBER);
            }
        }
    }
    public void validateExistMember(Invitation invitation, Member member){
        if(!invitation.getMembers().contains(member)){
            throw new DanggeunPlannerException(NOT_INVITED_MEMBER);
        }
    }
}
