package test.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String getCurrentFormattedDateTime() {
        return LocalDateTime.now().format(FORMATTER);
    }


    public static String formatLocalDateTime(String isoDateTime) {
        LocalDateTime dateTime = LocalDateTime.parse(isoDateTime);
        return dateTime.format(FORMATTER);
    }
}