package com.heaton.weekview.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.util.List;

public class ClassRepository implements ClassDataSource {
    private static volatile ClassRepository mInstance;

    private ClassDataSource remote;
    private ClassDataSource local;
    private static final String TAG = ClassRepository.class.getSimpleName();


    private ClassRepository(Context context) {
        remote = RemoteClassDataSource.getInstance();
//        local = LocalClassDataSource.getInstance(context);
    }

    public static ClassDataSource getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ClassRepository.class) {
                if (mInstance == null) {
                    mInstance = new ClassRepository(context);
                }
            }
        }
        return mInstance;
    }

    @Override
    public void getScheduleList(@NonNull String teacherName, @NonNull String startedAt
            , @NonNull final RemoteClassDataSource.GetScheduleCallback callback) {
        remote.getScheduleList(teacherName, startedAt, new RemoteClassDataSource.GetScheduleCallback() {
            @Override
            public void onSuccess(ScheduleJsonObject scheduleJsonObject) {
                callback.onSuccess(scheduleJsonObject);
//                local.updateScheduleList(teacherName, scheduleJsonObject.getAvailableList());
//                local.updateScheduleList(teacherName, scheduleJsonObject.getBookedList());
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
                //local.getScheduleList(teacherName, startedAt, callback);
            }
        });
    }

    @Override
    public void updateScheduleList(@NonNull String teacherName, @NonNull List<ClassInterval> intervalList) {
        //remote.updateScheduleList(teacherName, intervalList);
        //local.updateScheduleList(teacherName, intervalList);
    }
}
