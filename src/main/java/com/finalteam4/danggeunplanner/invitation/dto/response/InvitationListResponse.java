package com.finalteam4.danggeunplanner.invitation.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InvitationListResponse {
    List<InvitationResponse> invitations = new ArrayList<>();

    public void addInvitation(InvitationResponse response){
        this.invitations.add(response);
    }
}
