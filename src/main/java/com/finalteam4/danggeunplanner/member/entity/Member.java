package com.finalteam4.danggeunplanner.member.entity;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    
    @Column
    private String password;
    
    @Column
    private String username;
    
    @Column(name="profile_image",nullable = false)
    private String profileImage;
    
    @Column
    private Boolean isPlannerOpened = true;

    @Builder
    public Member(String email, String password, String username, String profileImage, Boolean isPlannerOpened){
        this.email = email;
        this.password = password;
        this.username = username;
        this.profileImage = profileImage;
        this.isPlannerOpened = isPlannerOpened;
    }
    
    public void updateUsername(String username){
        this.username = username;
    }
    
    public void updateProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
    
    public void updatePlannerOpened(Boolean plannerOpened ){
        this.isPlannerOpened = plannerOpened;
    }
    public boolean isPlannerOpened(){
        return this.isPlannerOpened;
    }
}
