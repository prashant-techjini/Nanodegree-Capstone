package com.nanodegree.topnews.util;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Prashant Nayak
 */

public class TimeUtils {

    public static String getDisplayTextTime(String timeString) {
        SimpleDateFormat format =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String time = "";
        try {
            Date date = format.parse(timeString);
            SimpleDateFormat printFormat = new SimpleDateFormat("MMM dd yyyy, hh:mm a", Locale.getDefault());
            printFormat.setTimeZone(TimeZone.getDefault());
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(new String[]{"AM", "PM"});
            printFormat.setDateFormatSymbols(symbols);
            time = printFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
}
