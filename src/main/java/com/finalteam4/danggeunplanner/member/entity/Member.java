package com.finalteam4.danggeunplanner.member.entity;


import com.finalteam4.danggeunplanner.invitation.entity.Invitation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column
    private String username;
    @Column(name="profile_image",nullable = false)
    private String profileImage;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="invitation_id")
    private Invitation invitation;

    public void confirmInvitation(Invitation invitation){
        /* 양방향 연관관계를 맺지 않는다 */
        this.invitation=invitation;
    }
    public Member(String email, String password){
        this.email = email;
        this.password = password;
        this.profileImage = "https://files.slack.com/files-pri/T01L2TNGW3T-F04HWRR7AUA/profile_pic.png";
    }
    public void updateUsername(String username){
        this.username = username;
    }
    public void updateProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
    public void deleteInvitation(){
        this.invitation=null;
    }
}
