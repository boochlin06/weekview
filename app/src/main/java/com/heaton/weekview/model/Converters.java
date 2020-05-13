package com.heaton.weekview.model;

import androidx.room.TypeConverter;

import com.heaton.weekview.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converters {
    static DateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_RESPONSE_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(String value) {
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
//
//    @TypeConverter
//    public static Date fromTimestamp(Long value) {
//        return value == null ? null : new Date(value);
//    }
//
//    @TypeConverter
//    public static Long dateToTimestamp(Date date) {
//        return date == null ? null : date.getTime();
//    }
}