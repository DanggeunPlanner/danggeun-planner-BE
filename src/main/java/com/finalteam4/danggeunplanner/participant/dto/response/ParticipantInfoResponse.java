package com.finalteam4.danggeunplanner.participant.dto.response;

import com.finalteam4.danggeunplanner.group.entity.Group;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParticipantInfoResponse {
    private Long groupId;
    private String groupName;
    private String onlineParticipant;
    private Boolean isAdmin;
    private List<ParticipantListResponse> myInfo = new ArrayList<>();
    private List<ParticipantListResponse> participantList = new ArrayList<>();

    public ParticipantInfoResponse(Group group, String onlineParticipant, Boolean isAdmin) {
        this.groupId = group.getId();
        this.groupName = group.getName();
        this.onlineParticipant = onlineParticipant;
        this.isAdmin = isAdmin;
    }
    public void addMyInfo(ParticipantListResponse response) {
        myInfo.add(response);
    }
    public void addParticipantList(ParticipantListResponse response){
        participantList.add(response);
    }
}
