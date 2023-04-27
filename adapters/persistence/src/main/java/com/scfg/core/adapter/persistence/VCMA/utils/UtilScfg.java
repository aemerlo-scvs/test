package com.scfg.core.adapter.persistence.VCMA.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilScfg {
    public static Timestamp convertToTimesStampHHMMSS(String request_date) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            // you can change format of date
            Date date = formatter.parse(request_date);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            return timeStampDate;
        } catch (ParseException e) {
            System.out.println("Exception :" + e);
            return null;
        }
    }
    public static Timestamp convertToTimesStamp(String dateString) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            Date date = formatter.parse(dateString);

            Timestamp timestamp = new Timestamp(date.getTime());
            return timestamp;
        }catch (ParseException e){
            System.out.println("Exception :" + e);
            return null;
        }

    }

    public static String convertTimesStampToString(Timestamp request_date) {
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(request_date);
        return formattedDate;
    }
    public static String convertTimesStampToStringHHMMSS(Timestamp request_date) {
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(request_date);
        return formattedDate;
    }
}
