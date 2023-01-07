package com.finalteam4.danggeunplanner.planner.entity;

import com.finalteam4.danggeunplanner.CustomDateTimeFormatter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="planner_id")
    private String id;
    @Column(nullable = false)
    private String date;
    @Column(nullable = false)
    private String owner;
    @Column(nullable = false)
    private Integer carrot;

    public Planner(Member member){
        this.date = CustomDateTimeFormatter.toYearAndMonthAndDayFormat(LocalDateTime.now());
        this.owner = member.getUsername();
        this.carrot=0;
    }
}
