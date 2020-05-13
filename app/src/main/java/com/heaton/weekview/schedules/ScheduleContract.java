package com.heaton.weekview.schedules;

import com.heaton.weekview.BasePresenter;
import com.heaton.weekview.BaseView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface ScheduleContract {
    interface View extends BaseView<Presenter> {
        void showSchedules(Calendar startDate, Map<Integer, List<ClassData>> schedules);

        void showQueryInterval(Calendar calendar);

        void showLoading(boolean isShow);

        void showToast(int id);
    }

    interface Presenter extends BasePresenter {
        void loadCurrentWeekSchedules();

        void loadNextWeekSchedules();

        void loadPreviousWeekSchedules();
    }
}
