package com.heaton.weekview.model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.heaton.weekview.model.localDataSource.LocalClassDataSource;
import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;

import java.util.List;

public class MainClassDataSource implements ClassDataSource {
    private static volatile MainClassDataSource mInstance;

    private ClassDataSource remote;
    private ClassDataSource local;
    private static final String TAG = MainClassDataSource.class.getSimpleName();


    private MainClassDataSource(Context context) {
        remote = RemoteClassDataSource.getInstance();
        local = LocalClassDataSource.getInstance(context);
    }

    public static ClassDataSource getInstance(Context context) {
        if (mInstance == null) {
            synchronized (MainClassDataSource.class) {
                if (mInstance == null) {
                    mInstance = new MainClassDataSource(context);
                }
            }
        }
        return mInstance;
    }

    @Override
    public void getScheduleList(@NonNull String teacherName, @NonNull String startedAt, @NonNull RemoteClassDataSource.GetListCallback callback) {
        remote.getScheduleList(teacherName, startedAt, new RemoteClassDataSource.GetListCallback() {
            @Override
            public void onSuccess(List<ClassInterval> intervalList) {
                callback.onSuccess(intervalList);
                local.updateScheduleList(teacherName, intervalList);
            }

            @Override
            public void onFailure(String errorMessage) {
                local.getScheduleList(teacherName,startedAt,callback);
            }
        });
    }

    @Override
    public void updateScheduleList(@NonNull String teacherName, @NonNull List<ClassInterval> intervalList) {
        remote.updateScheduleList(teacherName, intervalList);
        local.updateScheduleList(teacherName, intervalList);
    }
}
