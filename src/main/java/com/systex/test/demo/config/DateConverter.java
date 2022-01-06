package com.systex.test.demo.config;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {
    public static LocalDate DATE_TO_LOCALDATE(Date date){
        LocalDate dateToLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return dateToLocalDate;
    }

    public static String LOCALDATE_TO_STRING(LocalDate localDate, String symbol){
        String stringToDate = (localDate.getYear() - 1911) + symbol
                + String.format("%02d", localDate.getMonthValue()) + symbol
                + String.format("%02d", localDate.getDayOfMonth());
        return stringToDate;
    }

    public static LocalDate TIMESTAMP_TO_LOCALDATE(Timestamp timestamp){
        LocalDate timeStampToLocalDateTime = LocalDate.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        return timeStampToLocalDateTime;
    }
}
