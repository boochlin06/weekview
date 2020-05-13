package com.heaton.weekview.model;

import androidx.room.TypeConverter;

import com.heaton.weekview.constants.FormatConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeConverters {
    static DateFormat df = new SimpleDateFormat(FormatConstants.TIME_STAMP_RESPONSE_FORMAT);

    @TypeConverter
    public static Date fromStringToDate(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromDateToString(Date value) {
        return df.format(value);
    }
}