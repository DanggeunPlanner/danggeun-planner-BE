package com.finalteam4.danggeunplanner.planner.dto.response;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlannerResponse {
    private final Boolean isOwner;
    private final Boolean isPlannerOpened;
    private final String username;
    private final String profileImage;
    private final Integer carrot;
    private final List<Object> contents = new ArrayList<>();

    public PlannerResponse(Member other, Member member) {
        this.isOwner = other.getUsername().equals(member.getUsername());
        this.isPlannerOpened = other.isPlannerOpened();
        this.username = other.getUsername();
        this.profileImage = other.getProfileImage();
        this.carrot = 0;
    }

    public PlannerResponse(Planner planner, Member member) {
        this.isOwner = planner.getMember().getUsername().equals(member.getUsername());
        this.isPlannerOpened = planner.getMember().isPlannerOpened();
        this.username = planner.getMember().getUsername();
        this.profileImage = planner.getMember().getProfileImage();
        this.carrot = planner.getCarrot();
    }

    public void addPlan(PlanResponse response) {
        contents.add(response);
    }

    public void addTimer(TimerResponse response) {
        contents.add(response);
    }
}
