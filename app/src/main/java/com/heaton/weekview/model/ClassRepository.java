package com.heaton.weekview.model;

import androidx.annotation.NonNull;

import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.util.List;

public class ClassRepository implements ClassDataSource {
    private static volatile ClassRepository mInstance;

    private ClassDataSource remote;
    private ClassDataSource local;
    private static final String TAG = ClassRepository.class.getSimpleName();


    private ClassRepository(@NonNull ClassDataSource local
            , @NonNull ClassDataSource remote) {
        this.remote = remote;
        this.local = local;
    }

    public static ClassDataSource getInstance(@NonNull ClassDataSource local
            , @NonNull ClassDataSource remote) {
        if (mInstance == null) {
            synchronized (ClassRepository.class) {
                if (mInstance == null) {
                    mInstance = new ClassRepository(local, remote);
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
                local.updateScheduleList(teacherName, scheduleJsonObject.getAvailableList(), false);
                local.updateScheduleList(teacherName, scheduleJsonObject.getBookedList(), true);
            }

            @Override
            public void onFailure(String errorMessage) {
                local.getScheduleList(teacherName, startedAt, callback);
                callback.onFailure(errorMessage);
            }
        });
    }

    @Override
    public void updateScheduleList(@NonNull String teacherName, @NonNull List<ClassInterval> intervalList
            , boolean isBooked) {
        remote.updateScheduleList(teacherName, intervalList, isBooked);
        local.updateScheduleList(teacherName, intervalList, isBooked);
    }
}
