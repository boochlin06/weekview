package com.heaton.weekview.model;

import android.content.Context;

import com.heaton.weekview.model.localDataSource.LocalClassDataSource;
import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;

public class MainClassDataSource implements ClassDataSource {
    private static volatile MainClassDataSource mInstance;

    private ClassDataSource remote;
    private ClassDataSource local;
    private static final String TAG = MainClassDataSource.class.getSimpleName();

    private MainClassDataSource(Context context) {
        remote = RemoteClassDataSource.getInstance("");
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

}
