package com.finalteam4.danggeunplanner.calendar.entity;

import com.finalteam4.danggeunplanner.CustomDateTimeFormatter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
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
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="calendar_id")
    private Long id;

    private String date;
    private Integer carrot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @OneToMany(mappedBy = "calendar")
    private  List<Planner> planners = new ArrayList<>();

    public void addPlanner(Planner planner){
        planners.add(planner);
    }
    public Calendar(Member member){
        this.date = CustomDateTimeFormatter.toYearAndMonthFormat(LocalDateTime.now());
        this.member = member;
        this.carrot=0;
    }
    public void plusCarrot(){
        this.carrot++;
    }


}
