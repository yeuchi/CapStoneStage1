package com.example.ctyeung.capstonestage1.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static String getNow()
    {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        Date date = new Date();
        return dateFormat.format(date);
    }
}
