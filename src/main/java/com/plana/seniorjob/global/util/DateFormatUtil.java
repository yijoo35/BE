package com.plana.seniorjob.global.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateFormatUtil {

    private static final DateTimeFormatter API_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter TARGET_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public String formatDate(String yyyymmdd) {
        if (yyyymmdd == null || yyyymmdd.length() != 8) return yyyymmdd;

        try {
            LocalDate date = LocalDate.parse(yyyymmdd, API_FORMAT);
            return date.format(TARGET_FORMAT);
        } catch (Exception e) {
            return yyyymmdd;
        }
    }
}
