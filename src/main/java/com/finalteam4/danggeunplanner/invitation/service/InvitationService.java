package com.finalteam4.danggeunplanner.invitation.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.group.repository.GroupRepository;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationGroupResponse;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationListResponse;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationResponse;
import com.finalteam4.danggeunplanner.invitation.dto.response.InvitationSearchResponse;
import com.finalteam4.danggeunplanner.invitation.entity.Invitation;
import com.finalteam4.danggeunplanner.invitation.repository.InvitationRepository;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.member.service.MemberValidator;
import com.finalteam4.danggeunplanner.participant.entity.Participant;
import com.finalteam4.danggeunplanner.participant.repository.ParticipantRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_GROUP;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;
    private final MemberValidator memberValidator;
    private final InvitationValidator invitationValidator;
    @Transactional
    public void create(Long memberId,Long groupId) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        deleteExistInvitation(group);

        invitationValidator.validateAdmin(member,group);

        Invitation invitation = new Invitation(group);
        invitationRepository.save(invitation);
    }

    public InvitationListResponse find(Member member,Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        Invitation invitation = invitationRepository.findByGroup(group);
        invitationValidator.validateAdmin(member,group);

        InvitationListResponse response = new InvitationListResponse();

        for(Member other : invitation.getMembers()){
            response.addInvitation(new InvitationResponse(other));
        }

        return response;
    }
    @Transactional
    public InvitationResponse addMember(Member member,Long groupId, String username){

        Member other = memberRepository.findByUsername(username).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        Invitation invitation = invitationRepository.findByGroup(group);

        invitationValidator.validateAdmin(member,group);
        invitationValidator.validateMaxSize(invitation,group);
        invitationValidator.validateParticipant(group,other);
        invitationValidator.validateInvitedMember(invitation,other);

        invitation.addMember(other);
        return new InvitationResponse(other);
    }
    @Transactional
    public InvitationResponse removeMember(Member member, Long groupId, String username) {

        Member other = memberRepository.findByUsername(username).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_MEMBER)
        );
        Group group = groupRepository.findById(groupId).orElseThrow(
                ()->new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        Invitation invitation = invitationRepository.findByGroup(group);

        invitationValidator.validateAdmin(member,group);
        invitationValidator.validateExistMember(invitation,other);

        invitation.removeMember(other);

        return new InvitationResponse(other);
    }
    private void deleteExistInvitation(Group group){
        if(invitationRepository.existsByGroup(group)){
            Invitation invitation = invitationRepository.findByGroup(group);
            invitationRepository.delete(invitation);
        }
    }

    public InvitationGroupResponse searchMember(Member member, Long groupId, String username) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_GROUP)
        );
        InvitationGroupResponse response = new InvitationGroupResponse(group);

        List<Member> members = memberRepository.findByUsernameStartsWithOrderByUsername(username);
        for (Member searchMember : members) {
            if (searchMember.getId().equals(member.getId())){
                continue;
            }
            Optional<Participant> participant = participantRepository.findByMemberAndGroup(searchMember, group);
            boolean isMember = participant.isPresent();
            InvitationSearchResponse invitationSearchResponse = new InvitationSearchResponse(searchMember, isMember);
            response.addMembers(invitationSearchResponse);
        }
        return response;
    }
}
