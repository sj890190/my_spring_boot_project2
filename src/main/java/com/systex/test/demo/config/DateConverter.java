package com.systex.test.demo.config;

import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateConverter {
    public static Date STRING_TO_DATE(String date) throws ParseException {
        date = date.replace(".","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String day = date.substring(date.length() - 2);
        date = date.substring(0, date.length() - day.length());
        String month = date.substring(date.length() - 2);
        date = date.substring(0, date.length() - month.length());
        String year = String.valueOf((Integer.parseInt(date) + 1911));
        return sdf.parse(year + "-" + month + "-" + day);

    }

    public static LocalDate DATE_TO_LOCALDATE(Date date){
        LocalDate dateToLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return dateToLocalDate;
    }

    public static Date LOCALDATE_TO_DATE(LocalDate localDate){
        Date localDateToDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return localDateToDate;
    }

    public static String DATE_TO_STRING(Date localDate, String marketType){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateToString = dateFormat.format(localDate);
        return dateToString;
    }

    public static String LOCALDATE_TO_STRING(LocalDate localDate, String marketType){
        String symbol = "";
        if(marketType.equals("農")){
            symbol = ".";
        }
        String localDateToString = (localDate.getYear() - 1911) + symbol
                + String.format("%02d", localDate.getMonthValue()) + symbol
                + String.format("%02d", localDate.getDayOfMonth());
        return localDateToString;
    }

    public static long DATE_DIFF(Date date_start, Date date_end){
        LocalDate ld_start = DATE_TO_LOCALDATE(date_start);
        LocalDate ld_end = DATE_TO_LOCALDATE(date_end);
        long daysDiff = ld_start.until(ld_end, ChronoUnit.DAYS);
        return daysDiff;
    }

    public static LocalDate TIMESTAMP_TO_LOCALDATE(Timestamp timestamp){
        LocalDate timeStampToLocalDateTime = LocalDate.ofInstant(timestamp.toInstant(), ZoneId.systemDefault());
        return timeStampToLocalDateTime;
    }

    public static Date FORMAT_LOCALDATE(LocalDate localDate, String patten){
        localDate = LocalDate.parse(localDate.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Date localDateToDate = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return localDateToDate;
    }
}
