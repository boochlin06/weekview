package com.heaton.weekview.model;

import android.content.Context;
import android.util.Log;

import com.heaton.weekview.model.localDataSource.ClassDataBase;
import com.heaton.weekview.model.localDataSource.ClassIntervalDao;
import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.util.Date;
import java.util.List;

public class MockLocalClassDataSource implements ClassDataSource {

    private static final String TAG = MockLocalClassDataSource.class.getSimpleName();
    private static volatile MockLocalClassDataSource mInstance;
    private ClassIntervalDao classIntervalDao;

    private MockLocalClassDataSource(Context context) {
        this.classIntervalDao = ClassDataBase.getInstance(context).getClassIntervalDao();
    }

    public static MockLocalClassDataSource getInstance(Context context) {
        if (mInstance == null) {
            synchronized (MockLocalClassDataSource.class) {
                if (mInstance == null) {
                    mInstance = new MockLocalClassDataSource(context);
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
