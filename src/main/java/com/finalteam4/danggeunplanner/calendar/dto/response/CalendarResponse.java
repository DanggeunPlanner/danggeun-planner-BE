package com.finalteam4.danggeunplanner.calendar.dto.response;

import com.finalteam4.danggeunplanner.calendar.entity.Calendar;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CalendarResponse {
    private final String username;
    private final String profileImage;
    private final Integer carrot;
    private final List<ColorStageResponse> colorStages = new ArrayList<>();

    public CalendarResponse(Calendar calendar){
        this.username = calendar.getMember().getUsername();
        this.profileImage = calendar.getMember().getProfileImage();
        this.carrot = calendar.getCarrot();
    }
    public void addColorStage(ColorStageResponse colorStage){
        colorStages.add(colorStage);
    }
}
