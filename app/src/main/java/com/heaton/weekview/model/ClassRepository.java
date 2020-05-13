package com.heaton.weekview.model;

import androidx.annotation.NonNull;

import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClassRepository implements ClassDataSource {
    private static volatile ClassRepository mInstance;

    private ClassDataSource remote;
    private ClassDataSource local;
    private ExecutorService executorService;
    private static final String TAG = ClassRepository.class.getSimpleName();


    private ClassRepository(@NonNull ClassDataSource local
            , @NonNull ClassDataSource remote) {
        this.remote = remote;
        this.local = local;
        executorService = Executors.newSingleThreadExecutor();
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
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        local.updateScheduleList(teacherName, scheduleJsonObject.getAvailableList(), false);
                        local.updateScheduleList(teacherName, scheduleJsonObject.getBookedList(), true);
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage);
                local.getScheduleList(teacherName, startedAt, callback);
            }
        });
    }

    @Override
    public void updateScheduleList(@NonNull String teacherName, @NonNull List<ClassInterval> intervalList
            , boolean isBooked) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                remote.updateScheduleList(teacherName, intervalList, isBooked);
                local.updateScheduleList(teacherName, intervalList, isBooked);
            }
        });
    }
}
