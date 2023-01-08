package com.finalteam4.danggeunplanner.timer.entity;

import com.finalteam4.danggeunplanner.CustomDateTimeFormatter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
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

    @Column
    private String date;

    @Column(name="start_time")
    private String startTime;

    @Column(name="end_time")
    private String endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="planner_id")
    private Planner planner;

    public void confirmPlanner(Planner planner){
        this.planner = planner;
        planner.addTimer(this);
    }

    public Timer(Member member){
        this.date = CustomDateTimeFormatter.toYearAndMonthAndDayFormat(LocalDateTime.now());
        this.startTime = CustomDateTimeFormatter.toTimeFormat(LocalDateTime.now().minusMinutes(25));
        this.endTime = CustomDateTimeFormatter.toTimeFormat(LocalDateTime.now());
        this.member = member;
    }
}
