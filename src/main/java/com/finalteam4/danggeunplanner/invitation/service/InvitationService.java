package com.finalteam4.danggeunplanner.invitation.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationListResponse;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationResponse;
import com.finalteam4.danggeunplanner.invitation.entity.Invitation;
import com.finalteam4.danggeunplanner.invitation.repository.InvitationRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.ALREADY_INVITED_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.ALREADY_PARTICIPATED_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.EXCEED_INVITATION_MAX_SIZE;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_INVITATION;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_INVITED_MEMBER;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_VALID_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    @Transactional
    public void create(Member member,Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        validateAdmin(member,group);
        deleteExistInvitation(member);

        Invitation invitation = new Invitation(group);
        invitationRepository.save(invitation);
        member.confirmInvitation(invitation);
    }

    public InvitationListResponse find(Member member,Long groupId) {
        Invitation invitation = invitationRepository.findById(member.getInvitation().getId()).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_INVITATION)
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        validateAdmin(member,group);

        InvitationListResponse response = new InvitationListResponse();

        for(Member other : invitation.getMembers()){
            response.addInvitation(new InvitationResponse(other));
        }

        return response;
    }
    @Transactional
    public InvitationResponse addMember(Member member,Long groupId, String username){
        Invitation invitation = invitationRepository.findById(member.getInvitation().getId()).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_INVITATION)
        );
        Member other = memberRepository.findByUsername(username).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        validateAdmin(member,group);
        validateMaxSize(invitation,group);
        validateParticipant(group,other);
        validateInvitedMember(invitation,other);

        invitation.addMember(other);

        return new InvitationResponse(other);
    }
    @Transactional
    public InvitationResponse removeMember(Member member, Long groupId, String username) {
        Invitation invitation = invitationRepository.findById(member.getInvitation().getId()).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_INVITATION)
        );
        Member other = memberRepository.findByUsername(username).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );

        validateAdmin(member,group);
        validateExistMember(invitation,other);

        invitation.removeMember(other);

        return new InvitationResponse(other);
    }
    private void deleteExistInvitation(Member member){
        if(memberRepository.existsInvitationByUsername(member)){
            member.deleteInvitation();
        }
    }

    private void validateMaxSize(Invitation invitation, Group group){
        if(group.getParticipants().size() + invitation.getMembers().size()>30){
            throw new DanggeunPlannerException(EXCEED_INVITATION_MAX_SIZE);
        }
    }
    private void validateAdmin(Member member, Group group){
        if(!group.getAdmin().equals(member.getUsername())){
            throw new DanggeunPlannerException(NOT_VALID_ACCESS);
        }
    }

    private void validateInvitedMember(Invitation invitation, Member member){
        if(invitation.getMembers().contains(member)){
            throw new DanggeunPlannerException(ALREADY_INVITED_MEMBER);
        }
    }

    private void validateParticipant(Group group, Member member){
        for(Participant participant : group.getParticipants()){
            if(participant.getMember().getId().equals(member.getId())){
                throw new DanggeunPlannerException(ALREADY_PARTICIPATED_MEMBER);
            }
        }
    }

    private void validateExistMember(Invitation invitation, Member member){
        if(!invitation.getMembers().contains(member)){
            throw new DanggeunPlannerException(NOT_INVITED_MEMBER);
        }
    }
}
