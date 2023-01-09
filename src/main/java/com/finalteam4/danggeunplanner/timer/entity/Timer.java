package com.finalteam4.danggeunplanner.timer.entity;

import com.finalteam4.danggeunplanner.CustomDateTimeFormatter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.AccessLevel;
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

    public Timer(Member member){
        this.date = CustomDateTimeFormatter.toYearAndMonthAndDayFormat(LocalDateTime.now());
        this.startTime = CustomDateTimeFormatter.toTimeFormat(LocalDateTime.now().minusMinutes(25));
        this.endTime = CustomDateTimeFormatter.toTimeFormat(LocalDateTime.now());
        this.member = member;
    }
}
