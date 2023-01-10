package com.finalteam4.danggeunplanner.group.entity;

import com.finalteam4.danggeunplanner.group.participant.entity.Participant;
import com.finalteam4.danggeunplanner.member.entity.Member;
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
    public Group(String groupName, String description, Member member, String groupImage){
        this.groupName = groupName;
        this.admin = member.getUsername();
        this.description = description;
        this.groupImage = groupImage;
    }

    public void update(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
    }
}
