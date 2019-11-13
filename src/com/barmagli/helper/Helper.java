package com.barmagli.helper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author MohamedGamal
 */
public class Helper {

    /**
     * get current day
     *
     * @return
     */
    public static int getDay() {
        GregorianCalendar gcalendar = new GregorianCalendar();
        return gcalendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * get current month
     *
     * @return
     */
    public static int getMonth() {
        GregorianCalendar gcalendar = new GregorianCalendar();
        return gcalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * get current year
     *
     * @return
     */
    public static int getYear() {
        GregorianCalendar gcalendar = new GregorianCalendar();
        return gcalendar.get(Calendar.YEAR);
    }
    
    public static String[] getDaysOfMonth() {
        String[] days = new String[31];
        for (int i = 0; i < days.length; i++) {
            days[i] = (i + 1) + "";
        }
        return days;
    }
    
    public static String[] getYears() {
        String[] years = new String[getYear() - 2009];
        for (int i = 0,j=2010; i < years.length; i++,j++) {
            years[i] = j + "";
        }
        return years;
    }

    /**
     * get current year
     *
     * @param year
     * @param month
     * @param day
     * @return formated date
     */
    public static Date formatedDate(String year, String month, String day) {
        String date = year + "-" + month + "-" + day;
        Date formatedDate = Date.valueOf(date);
        return formatedDate;
    }
    
    /**
     * split date from yyyy-MM-dd to [yyyy-MM-dd]
     * 
     * @param date
     * @return 
     */
    public static String[] splitDate(String date) {
        return date.split("-");
    }
    
     /**
     * check if the string is number or not
     *
     * @param value
     * @return boolean
     */
    public static boolean isNumber(String value) {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * explode string to array
     *
     * @param string
     * @param split
     * @return tokens
     */
    public static String[] explode(String string, String split) {
        String[] tokens = string.split(split);
        return tokens;
    }

    /**
     * remove last characters from string
     *
     * @param str
     * @param chars
     * @return
     */
    public static String rTrim(String str, String chars) {
        //$ = remove (chars) at the end of (str)
        Pattern pattern = Pattern.compile(chars + "$");
        return pattern.matcher(str).replaceAll("");
    }

    /**
     * check if the object is blank(empty)
     *
     * @param obj
     * @return boolean
     */
    public static boolean isBlank(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            return (str.isEmpty());
        } else if (obj instanceof Map) {
            Map map = (Map) obj;
            return (map.isEmpty());
        } else {
            List list = (List) obj;
            return (list == null || list.isEmpty());
        }
    }
    
     /**
     * check if the object is not blank
     *
     * @param obj
     * @return boolean
     */
    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }

    /**
     * replace any white space in element of list with blank string
     *
     * @param str
     * @return Array List
     */
    public static ArrayList replaceSpacesWithBlanck(ArrayList str) {
        for (int i = 0; i < str.size(); i++) {
            String newELement = ((String) str.get(i)).replaceAll("[ ]+", "");
            str.set(i, newELement);
        }
        return str;
    }

    /**
     * get current date
     *
     * @return current date
     */
    public static Date getCurrentDate() {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        return date;
    }
}
