package com.finalteam4.danggeunplanner.calendar.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ColorStageResponse {
    private final List<String> colorStage1=new ArrayList<>();
    private final List<String> colorStage2=new ArrayList<>();
    private final List<String> colorStage3=new ArrayList<>();
    private final List<String> colorStage4=new ArrayList<>();

    public void addDateToColorStage1(String date){
        colorStage1.add(date);
    }
    public void addDateToColorStage2(String date){
        colorStage2.add(date);
    }
    public void addDateToColorStage3(String date){
        colorStage3.add(date);
    }
    public void addDateToColorStage4(String date){
        colorStage4.add(date);
    }
}
