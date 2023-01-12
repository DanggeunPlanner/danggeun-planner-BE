package com.finalteam4.danggeunplanner.member.entity;


import com.finalteam4.danggeunplanner.invitation.entity.Invitation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
        this.invitation=invitation;
    }
    
    @Builder
    public Member(String email, String password, String username, String profileImage){
        this.email = email;
        this.password = password;
        this.username = username;
        this.profileImage = profileImage;
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
