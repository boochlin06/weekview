package com.heaton.weekview;

import android.util.Log;

import com.heaton.weekview.constants.FormatConstants;
import com.heaton.weekview.model.ClassInterval;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;
import com.heaton.weekview.schedule.ClassData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {
    public static Map<Integer, List<ClassData>> convertScheduleDataToDayList(String teacherName
            , ScheduleJsonObject scheduleJsonObject) {
        Map<Integer, List<ClassData>> scheduleMaps = new HashMap<>();
        convertClassIntervalToClassDataList(teacherName, scheduleJsonObject.getAvailableList(), false, scheduleMaps);
        convertClassIntervalToClassDataList(teacherName, scheduleJsonObject.getBookedList(), true, scheduleMaps);
        for (int key : scheduleMaps.keySet()) {
            Collections.sort(scheduleMaps.get(key), new ClassDataComparator());
            scheduleMaps.put(key, scheduleMaps.get(key));
        }
        return scheduleMaps;
    }

    public static void convertClassIntervalToClassDataList(String teacherName
            , List<ClassInterval> classDataList, boolean isBooked
            , Map<Integer, List<ClassData>> scheduleMaps) {
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < classDataList.size(); i++) {
            ClassInterval interval = classDataList.get(i);
            for (long j = interval.getStartAt().getTime(); j < interval.getEndAt().getTime()
                    ; j += FormatConstants.CLASS_INTERVAL_IN_MILLS) {
                ClassData classData = new ClassData();
                classData.setBooked(isBooked);
                classData.setStartTime(new Date(j + calendar.getTimeZone().getRawOffset()));
                classData.setTeacherName(teacherName);
                calendar.setTime(classData.getStartTime());
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                List<ClassData> dayList = scheduleMaps.getOrDefault(dayOfMonth, new ArrayList<>());
                dayList.add(classData);
                scheduleMaps.put(dayOfMonth, dayList);
            }
        }
    }

    public static class ClassDataComparator implements Comparator<ClassData> {
        @Override
        public int compare(ClassData c1, ClassData c2) {
            return c1.getStartTime().compareTo(c2.getStartTime());
        }
    }
}
