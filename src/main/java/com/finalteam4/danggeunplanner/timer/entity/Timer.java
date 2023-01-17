package com.finalteam4.danggeunplanner.timer.entity;

import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import lombok.AccessLevel;
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
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="timer_id")
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String content;
    private Integer count;
    private Boolean isFinish;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="planner_id")
    private Planner planner;

    public void confirmPlanner(Planner planner){
        this.planner = planner;
        planner.addTimer(this);
    }

    @Builder
    public Timer(Member member, LocalDateTime startTime, String content, Integer count, Boolean isFinish){
        this.member = member;
        this.startTime = startTime;
        this.content = content;
        this.count = count;
        this.isFinish=isFinish;
    }

    public void finish(LocalDateTime endTime, Integer count){
        this.endTime=endTime;
        this.count=count;
        this.isFinish=true;
    }

    public void update(String content){
        this.content=content;
    }
}
