package com.heaton.weekview.schedules;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.heaton.weekview.R;
import com.heaton.weekview.constants.FormatConstants;
import com.heaton.weekview.injecter.Injection;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ScheduleActivity extends AppCompatActivity implements ScheduleContract.View {

    private static final String TAG = ScheduleActivity.class.getSimpleName();

    @BindView(R.id.txtAvailableTimes)
    TextView txtAvailableTimes;
    @BindView(R.id.btnPrevWeek)
    ImageButton btnPrevWeek;
    @BindView(R.id.btnNextWeek)
    ImageButton btnNextWeek;
    @BindView(R.id.txtWeekInterval)
    TextView txtWeekInterval;
    @BindView(R.id.txtTimezone)
    TextView txtTimezone;
    @BindView(R.id.linearSchedule)
    LinearLayout linearSchedule;
    @BindView(R.id.progressLoading)
    ProgressBar progressLoading;

    private ScheduleContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        setTitle(getString(R.string.actionbar_title, Injection.TEACHER_NAME));
        presenter = new SchedulePresenter(Injection.TEACHER_NAME
                , Injection.provideDataSource(getApplicationContext()), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    @OnClick({R.id.btnNextWeek, R.id.btnPrevWeek})
    public void onChangeDateButtonClick(View view) {
        int id = view.getId();
        if (R.id.btnPrevWeek == id) {
            presenter.loadPreviousWeekSchedules();
        } else if (R.id.btnNextWeek == id) {
            presenter.loadNextWeekSchedules();
        }
    }

    @Override
    public void setPresenter(ScheduleContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showSchedules(Calendar startDate, Map<Integer, List<ClassData>> schedulesMap) {
        Calendar scheduleStartDate = (Calendar) startDate.clone();
        Calendar today = Calendar.getInstance();
        DateFormat formatDayOfWeek = new SimpleDateFormat(FormatConstants.TIME_STAMP_DAY_OF_WEEK);
        scheduleStartDate.setTimeInMillis(scheduleStartDate.getTimeInMillis()
                + scheduleStartDate.getTimeZone().getRawOffset());
        linearSchedule.removeAllViews();
        for (int i = 0; i < FormatConstants.SCHEDULE_INTERVAL_DAYS; i++) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            View dayView = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.list_day_view, null);
            dayView.setLayoutParams(layoutParams);

            TextView txtDay = dayView.findViewById(R.id.txtDay);
            TextView txtSubDay = dayView.findViewById(R.id.txtSubDay);
            View imgAvailable = dayView.findViewById(R.id.imgDayAvailable);
            RecyclerView rcyTime = dayView.findViewById(R.id.rcyTime);
            List<ClassData> dayClassList = schedulesMap.getOrDefault(
                    scheduleStartDate.get(Calendar.DAY_OF_MONTH), new ArrayList<>());
            ClassDataAdapter adapter = new ClassDataAdapter(dayClassList);

            txtDay.setText(formatDayOfWeek.format(scheduleStartDate.getTime()));
            txtSubDay.setText(String.valueOf(scheduleStartDate.get(Calendar.DAY_OF_MONTH)));
            imgAvailable.setBackgroundColor(dayClassList.size() > 0
                    || today.get(Calendar.DAY_OF_YEAR) == scheduleStartDate.get(Calendar.DAY_OF_YEAR)
                    ? getColor(R.color.colorAccent) : Color.GRAY);
            rcyTime.setAdapter(adapter);
            scheduleStartDate.add(Calendar.DAY_OF_MONTH, 1);
            linearSchedule.addView(dayView);
        }
    }

    @Override
    public void showQueryInterval(Calendar startDate) {
        Calendar scheduleStartDate = (Calendar) startDate.clone();
        scheduleStartDate.setTimeInMillis(scheduleStartDate.getTimeInMillis() + scheduleStartDate.getTimeZone().getRawOffset());
        SimpleDateFormat format = new SimpleDateFormat(FormatConstants.TIME_STAMP_DAYS);
        String start = format.format(scheduleStartDate.getTime());
        scheduleStartDate.add(Calendar.DAY_OF_MONTH, FormatConstants.SCHEDULE_INTERVAL_DAYS);
        txtWeekInterval.setText(start + " - " + scheduleStartDate.get(Calendar.DAY_OF_MONTH));
        txtTimezone.setText(getString(R.string.time_zone_hint, Locale.getDefault().getDisplayCountry()
                + scheduleStartDate.getTimeZone().getDisplayName(true, TimeZone.SHORT)));
        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.WEEK_OF_YEAR) + 1 >= scheduleStartDate.get(Calendar.WEEK_OF_YEAR)) {
            btnPrevWeek.setClickable(false);
            btnPrevWeek.setAlpha(0.2f);
        } else {
            btnPrevWeek.setClickable(true);
            btnPrevWeek.setAlpha(1.0f);
        }
    }

    @Override
    public void showLoading(boolean isShow) {
        progressLoading.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        progressLoading.setIndeterminate(isShow);
    }

    @Override
    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_LONG).show();
    }
}
