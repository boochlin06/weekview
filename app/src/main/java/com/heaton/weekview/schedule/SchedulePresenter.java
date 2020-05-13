package com.heaton.weekview.schedule;

import android.util.Log;

import com.heaton.weekview.Constants;
import com.heaton.weekview.R;
import com.heaton.weekview.Utility;
import com.heaton.weekview.model.ClassDataSource;
import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;
import com.heaton.weekview.model.remoteDataSource.ScheduleJsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SchedulePresenter implements ScheduleContract.Presenter {
    private static final String TAG = SchedulePresenter.class.getSimpleName();
    private ClassDataSource classDataSource;
    private ScheduleContract.View view;
    private String teacherName;
    private Calendar currentQueryDate;

    public SchedulePresenter(String teacherName
            , ClassDataSource classDataSource
            , ScheduleContract.View scheduleView) {
        this.classDataSource = classDataSource;
        this.teacherName = teacherName;
        this.view = scheduleView;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        currentQueryDate = Calendar.getInstance();
        currentQueryDate.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        loadCurrentWeekSchedules();
    }

    @Override
    public void loadCurrentWeekSchedules() {
        currentQueryDate.set(Calendar.HOUR_OF_DAY, 0);
        currentQueryDate.set(Calendar.MINUTE, 0);
        currentQueryDate.set(Calendar.SECOND, 0);
        currentQueryDate.setTimeInMillis(currentQueryDate.getTimeInMillis()
                - currentQueryDate.getTimeZone().getRawOffset());
        loadSchedules();
        view.showQueryInterval(currentQueryDate);
    }

    @Override
    public void loadNextWeekSchedules() {
        int i = currentQueryDate.get(Calendar.WEEK_OF_MONTH);
        currentQueryDate.set(Calendar.WEEK_OF_MONTH, ++i);
        loadSchedules();
    }

    @Override
    public void loadPreviousWeekSchedules() {
        int i = currentQueryDate.get(Calendar.WEEK_OF_MONTH);
        currentQueryDate.set(Calendar.WEEK_OF_MONTH, --i);
        loadSchedules();
    }

    private void loadSchedules() {
        view.showLoading(true);
        DateFormat format = new SimpleDateFormat(Constants.TIME_STAMP_QUERY_STRING_FORMAT);
        String startAt = format.format(currentQueryDate.getTime());
        classDataSource.getScheduleList(teacherName, startAt
                , new RemoteClassDataSource.GetScheduleCallback() {
                    @Override
                    public void onSuccess(ScheduleJsonObject scheduleJsonObject) {
                        view.showSchedules(currentQueryDate
                                , Utility.convertScheduleDataToDayList(teacherName, scheduleJsonObject));
                        view.showLoading(false);
                        view.showQueryInterval(currentQueryDate);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.e(TAG, "error:" + errorMessage);
                        view.showToast(R.string.network_error);
                        view.showLoading(false);
                    }
                });
    }
}
