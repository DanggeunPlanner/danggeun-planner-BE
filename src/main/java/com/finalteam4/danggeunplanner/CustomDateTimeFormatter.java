package com.finalteam4.danggeunplanner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    public static String toYearAndMonthFormat(LocalDateTime localDateTime) {
        java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM");
        return localDateTime.format(dateFormatter);
    }
    public static String toYearAndMonthAndDayFormat(LocalDateTime localDateTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(dateFormatter);
    }
    public static String toTimeFormat(LocalDateTime localDateTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(timeFormatter);
    }

}
