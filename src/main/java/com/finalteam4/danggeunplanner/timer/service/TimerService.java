package com.finalteam4.danggeunplanner.timer.service;


import com.finalteam4.danggeunplanner.TimeConverter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.timer.dto.response.TimerResponse;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_CALENDAR;
import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_PLANNER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimerService {
    private final TimerRepository timerRepository;
    private final PlannerRepository plannerRepository;
    private final CalendarRepository calendarRepository;
    @Transactional
    public TimerResponse finish(Member member){
        Timer timer = new Timer(member);
        timerRepository.save(timer);

        createPlanner(member);
        Planner planner = plannerRepository.findByMemberAndDate(member, TimeConverter.getCurrentTimeToYearMonthDay()).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_PLANNER));
        timer.confirmPlanner(planner);
        planner.addCarrot();

        createCalendar(member);
        Calendar calendar = calendarRepository.findByMemberAndDate(member, TimeConverter.getCurrentTimeToYearMonth()).orElseThrow(() -> new DanggeunPlannerException(NOT_FOUND_CALENDAR));
        planner.confirmCalendar(calendar);
        calendar.addCarrot();

        return new TimerResponse(timer);
    }

    private void createPlanner(Member member){
        if(!plannerRepository.existsByMemberAndDate(member, TimeConverter.getCurrentTimeToYearMonthDay())) {
            Planner planner = new Planner(member, TimeConverter.getCurrentTimeToYearMonthDay());
            plannerRepository.save(planner);
        }
    }

    private void createCalendar(Member member){
        if(!calendarRepository.existsByMemberAndDate(member, TimeConverter.getCurrentTimeToYearMonth())) {
            Calendar calendar = new Calendar(member);
            calendarRepository.save(calendar);
        }
    }
}
