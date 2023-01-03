package com.finalteam4.danggeunplanner.pomodoro.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pomodoro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pomodoro_id")
    private Long id;
}
