package com.capstone.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public static String getCurrentFormattedDateTime() {
        return LocalDateTime.now().format(FORMATTER);
    }


    public static String formatLocalDateTime(String isoDateTime) {
        LocalDateTime dateTime = LocalDateTime.parse(isoDateTime);
        return dateTime.format(FORMATTER);
    }

    public static String formatIsoDateTime(LocalDateTime localDateTime){
        return localDateTime.format(TIMESTAMP_FORMATTER);
    }
}