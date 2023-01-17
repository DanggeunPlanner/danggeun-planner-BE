package com.finalteam4.danggeunplanner.calendar.service;

import com.finalteam4.danggeunplanner.calendar.dto.response.CalendarResponse;
import com.finalteam4.danggeunplanner.calendar.dto.response.ColorStageResponse;
import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.calendar.repository.CalendarRepository;
import com.finalteam4.danggeunplanner.common.exception.DanggeunPlannerException;
import com.finalteam4.danggeunplanner.member.entity.Member;
import com.finalteam4.danggeunplanner.member.repository.MemberRepository;
import com.finalteam4.danggeunplanner.planner.entity.Planner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.finalteam4.danggeunplanner.common.exception.ErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    public CalendarResponse find(String username, String date) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new DanggeunPlannerException(NOT_FOUND_MEMBER));

        Optional<Calendar> calendar = calendarRepository.findByMemberAndDate(member, date);

        if(calendar.isPresent()) {
            CalendarResponse response = new CalendarResponse(calendar.get());
            ColorStageResponse colorStage = new ColorStageResponse();

            addDateToColorStage(calendar.get(), colorStage);
            response.addColorStage(colorStage);

            return response;
        }

        return new CalendarResponse(member);
    }

    private void addDateToColorStage(Calendar calendar, ColorStageResponse colorStage){
        for (Planner planner : calendar.getPlanners()) {
            Integer carrot = planner.getCarrot();
            String date = planner.getDate();

            if ((0 < carrot && carrot <= 4)) {
                colorStage.addDateToColorStage1(date);
            } else if ((4 < carrot && carrot <= 8)) {
                colorStage.addDateToColorStage2(date);
            } else if ((8 < carrot && carrot <= 12)) {
                colorStage.addDateToColorStage3(date);
            } else if ((12 < carrot)) {
                colorStage.addDateToColorStage4(date);
            }
        }
    }
}




