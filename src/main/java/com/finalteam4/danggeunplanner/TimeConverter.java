package com.finalteam4.danggeunplanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeConverter {

    public static String getCurrentTimeToYearMonth() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return LocalDateTime.now().format(formatter);
    }
    public static String getCurrentTimeToYearMonthDay() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDateTime.now().format(formatter);
    }
    public static String changeTimeToHourMinute(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

}
