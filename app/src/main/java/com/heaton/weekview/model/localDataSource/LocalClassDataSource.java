package com.heaton.weekview.model.localDataSource;

import android.content.Context;
import android.util.Log;

import com.heaton.weekview.model.ClassDataSource;
import com.heaton.weekview.model.ClassInterval;
import com.heaton.weekview.model.DateConverters;
import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.util.Date;
import java.util.List;

public class LocalClassDataSource implements ClassDataSource {

    private static final String TAG = LocalClassDataSource.class.getSimpleName();
    private static volatile LocalClassDataSource mInstance;
    private ClassIntervalDao classIntervalDao;

    private LocalClassDataSource(Context context) {
        this.classIntervalDao = ClassDataBase.getInstance(context).getClassIntervalDao();
    }

    public static LocalClassDataSource getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LocalClassDataSource.class) {
                if (mInstance == null) {
                    mInstance = new LocalClassDataSource(context);
                }
            }
        }
        return mInstance;
    }

    @Override
    public void getScheduleList(String teacherName, String startedAt
            , RemoteClassDataSource.GetScheduleCallback callback) {
        try {
            Log.d(TAG, "startedAt" + startedAt);
            Date startDate = DateConverters.fromStringToDate(startedAt);
            Date endAt = new Date(startDate.getTime() + 7 * 24 * 3600 * 1000);
            List<ClassInterval> bookedList = classIntervalDao.getClassIntervals(teacherName
                    , startDate, endAt, true);
            List<ClassInterval> availableList = classIntervalDao.getClassIntervals(teacherName
                    , startDate, endAt, false);
            ScheduleJsonObject scheduleJsonObject = new ScheduleJsonObject();
            scheduleJsonObject.setAvailableList(availableList);
            scheduleJsonObject.setBookedList(bookedList);
            callback.onSuccess(scheduleJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void updateScheduleList(String teacherName, List<ClassInterval> intervalList
            , boolean isBooked) {
        for (int i = 0; i < intervalList.size(); i++) {
            intervalList.get(i).setTeacherName(teacherName);
            intervalList.get(i).setBooked(isBooked);
        }
        classIntervalDao.insertAll(intervalList);
    }
}
