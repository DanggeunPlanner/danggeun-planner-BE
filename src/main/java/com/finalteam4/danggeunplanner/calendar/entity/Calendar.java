package com.finalteam4.danggeunplanner.calendar.entity;

import com.finalteam4.danggeunplanner.CustomDateTimeFormatter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    private String owner;

    private Integer carrot;

    public Calendar(Member member){
        this.date = CustomDateTimeFormatter.toYearAndMonthFormat(LocalDateTime.now());
        this.owner = member.getUsername();
        this.carrot=0;
    }



}
