package com.finalteam4.danggeunplanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {

    public static String convertToPlannerDateForm(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }
    public static String convertToCalendarDateForm(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return localDateTime.format(formatter);
    }
    public static String convertToHourMinute(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

}
