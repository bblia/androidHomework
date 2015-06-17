package com.liazadoyan.eventfinder.ApiObjects;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liazadoyan on 6/16/15.
 */
public class DatetimeTZ {
    private String timezone;
    private String local;
    private String utc;

    public String getLocal() throws ParseException {
        return local;
    }

    public String getTimeString() throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm aa", Locale.US);
        SimpleDateFormat tzsdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date = tzsdf.parse(local);
        String dateString = sdfDate.format(date);
        String timeString = sdfTime.format(date);
        return dateString + " at " + timeString;
    }
}
