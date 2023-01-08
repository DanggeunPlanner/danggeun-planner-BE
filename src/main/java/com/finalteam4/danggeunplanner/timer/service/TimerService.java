package com.finalteam4.danggeunplanner.timer.service;

import com.finalteam4.danggeunplanner.CustomDateTimeFormatter;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import com.finalteam4.danggeunplanner.planner.repository.PlannerRepository;
import com.finalteam4.danggeunplanner.timer.entity.Timer;
import com.finalteam4.danggeunplanner.timer.repository.TimerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public void finish(Member member){
        Timer timer = new Timer(member);
        timerRepository.save(timer);

        createPlanner(member);
        Planner planner = findPlanner(member);
        timer.confirmPlanner(planner);
        planner.plusCarrot();

        createCalendar(member);
        Calendar calendar = findCalendar(member);
        planner.confirmCalendar(calendar);
        calendar.plusCarrot();
    }


    private void createPlanner(Member member){
        if(!plannerRepository.existsByMemberAndDate(member,CustomDateTimeFormatter.toYearAndMonthAndDayFormat(LocalDateTime.now()))) {
            Planner planner = new Planner(member,CustomDateTimeFormatter.toYearAndMonthAndDayFormat(LocalDateTime.now()));
            plannerRepository.save(planner);
        }
    }

    private Planner findPlanner(Member member){
        return plannerRepository.findByMemberAndDate
                (member, CustomDateTimeFormatter.toYearAndMonthAndDayFormat(LocalDateTime.now())).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_PLANNER)
        );
    }

    private void createCalendar(Member member){
        if(!calendarRepository.existsByMemberAndDate(member,CustomDateTimeFormatter.toYearAndMonthFormat(LocalDateTime.now()))) {
            Calendar calendar = new Calendar(member);
            calendarRepository.save(calendar);
        }
    }

    private Calendar findCalendar(Member member){
        return calendarRepository.findByMemberAndDate
                (member, CustomDateTimeFormatter.toYearAndMonthFormat(LocalDateTime.now())).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_CALENDAR)
        );
    }

}
