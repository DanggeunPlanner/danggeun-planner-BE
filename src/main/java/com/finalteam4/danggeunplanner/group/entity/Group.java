package com.finalteam4.danggeunplanner.group.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="group_id")
    private Long id;

    @Column(nullable = false)
    private String groupName;

    @Column(nullable = false)
    private String admin;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String groupImage;

    @OneToMany(mappedBy = "group")
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public Group(String groupName, String description, String admin, String groupImage){
        this.groupName = groupName;
        this.admin = admin;
        this.description = description;
        this.groupImage = groupImage;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    public void update(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }

    public void updateAdmin(String username) {
        this.admin = username;
    }
}
