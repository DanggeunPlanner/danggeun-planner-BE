package com.finalteam4.danggeunplanner.invitation.entity;

import com.finalteam4.danggeunplanner.group.entity.Group;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;
    @OneToOne
    @JoinColumn(name="group_id")
    private Group group;
    @OneToMany(mappedBy="invitation")
    List<Member> members = new ArrayList<>();

    public Invitation(Group group){
        this.group = group;
    }
    public void addMember(Member member){
        members.add(member);
    }
    public void removeMember(Member member){
        members.remove(member);
    }
}
