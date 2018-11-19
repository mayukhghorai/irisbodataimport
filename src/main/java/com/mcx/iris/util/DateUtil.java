package com.mcx.iris.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ext-MayukhG on 16-03-2018.
 */
public class DateUtil {
    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return (dateFormat.format(date));
    }

    public static String getYesterday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return (dateFormat.format(yesterday()));
    }

    public static Date convertStringToDate(String strDate) {
        Date dt = null;
        StringBuffer sb = new StringBuffer(strDate);
        if(!strDate.equals("")) {
            sb = sb.insert(2, "-");
            sb.insert(6, "-");

            try {
                dt = new SimpleDateFormat("dd-MMM-yyyy").parse(sb.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dt;
    }

    public static Date yesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static Date yesterday(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static String yesterdayMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        String monthName = dateFormat.format(cal.getTime());
        return monthName;
    }

    public static Integer getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return (cal.get(Calendar.YEAR));
    }

    public static Integer getCurrentYear(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return (cal.get(Calendar.YEAR));
    }

    public static String getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        String monthName = dateFormat.format(cal.getTime());
        return monthName;
    }

    public static String getCurrentMonth(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        DateFormat dateFormat = new SimpleDateFormat("MMMM");
        String monthName = dateFormat.format(cal.getTime());
        return monthName;
    }

    public static Integer getMonth(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.MONTH);
    }

}