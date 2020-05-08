package com.heaton.weekview.model.localDataSource;

import android.content.Context;

import com.heaton.weekview.model.ClassDataSource;

public class LocalClassDataSource implements ClassDataSource {

    private static final String TAG = LocalClassDataSource.class.getSimpleName();
    private static volatile LocalClassDataSource mInstance;

    private LocalClassDataSource(Context context) {
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

}
