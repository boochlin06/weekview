package com.heaton.weekview;

import android.app.Application;

public class ScheduleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Stetho.initializeWithDefaults(this);
    }
}
