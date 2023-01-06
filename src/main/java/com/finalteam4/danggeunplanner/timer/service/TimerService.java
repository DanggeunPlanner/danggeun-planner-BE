package com.finalteam4.danggeunplanner.timer.service;

import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimerService {
    private final TimerRepository timerRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void finish(Long id){
        Member member = validatedMemberById(id);
        Timer pomodoro = new Timer(member);
        timerRepository.save(pomodoro);
    }

    private Member validatedMemberById(Long id){
        return memberRepository.findById(id).orElseThrow(
                ()-> new DanggeunPlannerException(NOT_FOUND_MEMBER));
    }

}
