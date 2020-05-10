package com.heaton.weekview;

import androidx.annotation.NonNull;

import com.heaton.weekview.model.ClassData;
import com.heaton.weekview.model.ClassInterval;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    public static Date convertClassIntervalToDate(@NonNull String dateString) {
        //"2020-05-10T17:00:00Z"
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ssZ");
        try {
            Date date = simpleDateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<Integer, List<ClassData>> convertScheduleDataToDayList(ScheduleJsonObject scheduleJsonObject) {
        List<ClassInterval> available = scheduleJsonObject.getAvailableList();
        List<ClassInterval> booked = scheduleJsonObject.getBookedList();
        Map<Integer, List<ClassData>> week = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < available.size(); i++) {
            Date date = convertClassIntervalToDate(available.get(i).getStartAt());
            ClassData classData = new ClassData();
            classData.setBooked(false);
            classData.setDate(date);
            calendar.setTime(date);
            calendar.get(Calendar.DAY_OF_WEEK);

        }
    }
}
