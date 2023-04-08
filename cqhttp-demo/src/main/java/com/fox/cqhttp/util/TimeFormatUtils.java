package com.fox.cqhttp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 狐狸半面添
 * @create 2023-04-08 10:50
 */
public class TimeFormatUtils {
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime localDateTime){
        return localDateTime.format(DATE_TIME_FORMATTER);
    }
}
