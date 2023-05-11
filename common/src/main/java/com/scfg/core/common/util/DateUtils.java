package com.scfg.core.common.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asDateToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asDateToLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    public static Timestamp asLocalDateTimeStamp(LocalDate localDate){
        return Timestamp.valueOf(localDate.atStartOfDay());
    }
    public  static Date asSummaryRestartDays(Date date, int parameter, int value){
        if (value==0) return date;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(parameter, value);
        return calendar.getTime();
    }
    public static Calendar getDateNowByGregorianCalendar(){
        Calendar calendar = GregorianCalendar.getInstance(Locale.forLanguageTag("es-BO"));
        return calendar;
    }

    public static Date changeHourInDateMorningAndNight(Date date, boolean morning, boolean night) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(morning) {
            cal.set(Calendar.HOUR_OF_DAY,00);
            cal.set(Calendar.MINUTE,00);
            cal.set(Calendar.SECOND,00);
        }
        if(night) {
            cal.set(Calendar.HOUR_OF_DAY,23);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.SECOND,59);
        }

        return cal.getTime();
    }
}
