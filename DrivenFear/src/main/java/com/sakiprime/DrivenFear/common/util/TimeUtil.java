package com.sakiprime.DrivenFear.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final DateTimeFormatter SECOND_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("Asia/Shanghai"));//时区

    public static String nowSecond() {
        return LocalDateTime.now().format(SECOND_FORMATTER);
    }

    public static long nowSecondTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}