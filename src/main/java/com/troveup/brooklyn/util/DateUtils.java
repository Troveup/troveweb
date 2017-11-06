package com.troveup.brooklyn.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by tim on 5/25/15.
 */
public class DateUtils
{
    //For use with the TimeZone object
    public static String TROVE_TIMEZONE = "America/New_York";

    public static Date convertIsoDate(String date)
    {
        Date rval = null;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            rval = df.parse(date);
        } catch (ParseException e) {
            //Eat the exception
        }
        return rval;

    }

    public static Date getTomorrowDateObject()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    public static Date getDateObjectHoursAgo(int hours)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    public static Date getDateEightDaysFrom(Date fromDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.DATE, 8);
        return cal.getTime();
    }

    public static Date getDateTwoDaysFrom(Date fromDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.DATE, 2);
        return cal.getTime();
    }

    public static Date getDateFiveMinutesFrom(Date fromDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.MINUTE, 5);
        return cal.getTime();
    }

    public static Date getDateXMinutesFrom(Date fromDate, int x)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.MINUTE, x);
        return cal.getTime();
    }

    public static Long getMinutesDelta(Date beginningDate, Date endDate)
    {
        Long millisDelta = endDate.getTime() - beginningDate.getTime();
        return millisDelta/60000;
    }

    public static Date getThirtySixHoursAgo(Date fromDate)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.HOUR, -36);
        return cal.getTime();
    }

    public static Date getDateXHoursAgo(Date fromDate, int x)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.HOUR, -x);
        return cal.getTime();
    }

    public static Date getDateXHoursFrom(Date fromDate, int x)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.HOUR, x);
        return cal.getTime();
    }

    public static Date getDateXDaysFrom(Date fromDate, int x)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fromDate);
        cal.add(Calendar.DATE, x);
        return cal.getTime();
    }

    public static TimeZone getTroveTimezone()
    {
        return TimeZone.getTimeZone(TROVE_TIMEZONE);
    }

    public static Integer getCurrentYear()
    {
        Calendar calendar = new GregorianCalendar(getTroveTimezone());
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    public static Date convertCommonHtmlCalendarDateToDateObject(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.parse(date);
    }

    public static String convertCommonDateObjectToCommonHtmlCalendarDateString(Date date)
    {
        String rval = null;

        if (date != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            rval = df.format(date);
        }

        return rval;
    }

    public static String formatRawDateOutputToESTString(Date date) {
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("AMERICA/NEW_YORK"));
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("AMERICA/NEW_YORK"));

        return sdf.format(calendar.getTime());
    }
}
