package com.heaton.weekview.model.localDataSource;

import android.content.Context;
import android.util.Log;

import com.heaton.weekview.constants.FormatConstants;
import com.heaton.weekview.model.ClassDataSource;
import com.heaton.weekview.model.ClassInterval;
import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
            DateFormat dateFormat = new SimpleDateFormat(FormatConstants.TIME_STAMP_QUERY_STRING_FORMAT);
            Log.d(TAG, "start at:" + startedAt);
            Date startDate = dateFormat.parse(startedAt);
            Date endAt = new Date(startDate.getTime() + FormatConstants.SCHEDULE_INTERVAL_DAYS * 24 * 3600 * 1000);
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
