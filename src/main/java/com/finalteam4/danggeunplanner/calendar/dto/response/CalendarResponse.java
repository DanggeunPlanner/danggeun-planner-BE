package com.finalteam4.danggeunplanner.calendar.dto.response;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import com.finalteam4.danggeunplanner.member.entity.Member;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CalendarResponse {
    private final String username;
    private final String profileImage;
    private final Integer carrot;
    private Integer todayColorStage;
    private final List<ColorStageResponse> colorStages = new ArrayList<>();


    public CalendarResponse(Member member){
        this.username=member.getUsername();
        this.profileImage=member.getProfileImage();
        this.carrot=0;
        this.todayColorStage =0;
    }
    public CalendarResponse(Calendar calendar){
        this.username = calendar.getMember().getUsername();
        this.profileImage = calendar.getMember().getProfileImage();
        this.carrot = calendar.getCarrot();
        this.todayColorStage=0;
    }
    public void addColorStage(ColorStageResponse colorStage){
        colorStages.add(colorStage);
    }
    public void addTodayColorStage(Integer carrot){
        if(0<carrot&carrot<=4) todayColorStage=1;
        if(4<carrot&carrot<=8) todayColorStage=2;
        if(8<carrot&carrot<=12) todayColorStage=3;
        if(12<carrot) todayColorStage=4;
    }
}
