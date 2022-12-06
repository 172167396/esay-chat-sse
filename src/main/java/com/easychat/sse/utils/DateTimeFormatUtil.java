package com.easychat.sse.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatUtil {

    public static final DateTimeFormatter HM = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter MD = DateTimeFormatter.ofPattern("M-d");
    public static final DateTimeFormatter CN_DATE = DateTimeFormatter.ofPattern("yyyy年M月d天");
    public static final DateTimeFormatter SLASH_DATE = DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss");

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().format(MD));
    }

    public static String getDisplayDate(LocalDateTime dateTime) {
        LocalDate date = LocalDate.from(dateTime);
        boolean today = LocalDate.now().equals(date);
        if (today) {
            return "今天";
        }
        boolean yesterday = LocalDate.now().minusDays(1).equals(date);
        if (yesterday) {
            return "昨天";
        }
        return date.format(CN_DATE);
    }
}
