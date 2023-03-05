package com.finalteam4.danggeunplanner.plan.domain;

import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.entity.Plan;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlanRepository;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlannerRepository plannerRepository;

    @BeforeEach
    public void setData(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(15);
        String date = TimeConverter.convertToPlannerDateForm(startTime);
        String content = "테스트";

        Member member = Member.builder()
                .email("email")
                .password("1234")
                .username("username")
                .profileImage("profileImage")
                .isPlannerOpened(true)
                .build();

        Member memberPersistence = memberRepository.save(member);

        Planner planner = Planner.builder()
                .date(date)
                .member(member)
                .build();

        Planner plannerPersistence = plannerRepository.save(planner);

        Plan plan =Plan.builder()
                .startTime(startTime)
                .endTime(endTime)
                .date(date)
                .content(content)
                .member(memberPersistence)
                .planner(plannerPersistence)
                .build();

        Plan planPersistence = planRepository.save(plan);

        assertThat(startTime).isEqualTo(planPersistence.getStartTime());
    }

    @Test
    @DisplayName("계획 저장")
    public void savePlan(){
        //given
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(15);
        String date = TimeConverter.convertToPlannerDateForm(startTime);
        String content = "테스트";

        //when
        Member member = Member.builder()
                .email("email")
                .password("1234")
                .username("username")
                .profileImage("profileImage")
                .isPlannerOpened(true)
                .build();

        Member memberPersistence = memberRepository.save(member);

        Planner planner = Planner.builder()
                .date(date)
                .member(member)
                .build();

        Planner plannerPersistence = plannerRepository.save(planner);

        Plan plan =Plan.builder()
                .startTime(startTime)
                .endTime(endTime)
                .date(date)
                .content(content)
                .member(memberPersistence)
                .planner(plannerPersistence)
                .build();

        Plan planPersistence = planRepository.save(plan);

        //then
        assertThat(startTime).isEqualTo(planPersistence.getStartTime());

    }
    //계획 수정
    @Test
    @DisplayName("계획 수정")
    public void updatePlan(){
    }


    //계획 삭제
}
