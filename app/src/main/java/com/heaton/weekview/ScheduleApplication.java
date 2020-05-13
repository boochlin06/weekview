package com.heaton.weekview;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class ScheduleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
