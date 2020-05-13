package com.heaton.weekview.schedule;

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

import com.heaton.weekview.Constants;
import com.heaton.weekview.Injection;
import com.heaton.weekview.R;

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

import static com.heaton.weekview.Constants.MOCK_TEACHER_NAME;

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
        presenter = new SchedulePresenter(MOCK_TEACHER_NAME
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
        scheduleStartDate.setTimeInMillis(scheduleStartDate.getTimeInMillis() + scheduleStartDate.getTimeZone().getRawOffset());
        SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("EEE");
        linearSchedule.removeAllViews();
        int startDayOfMonth = scheduleStartDate.get(Calendar.DAY_OF_MONTH);
        for (int i = startDayOfMonth; i < startDayOfMonth + Constants.SCHEDULE_INTERVAL_DAYS; i++) {
            List<ClassData> dayClass = schedulesMap.getOrDefault(i, new ArrayList<>());
            View dayView = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.list_day_view, null);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT
                    , LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            dayView.setLayoutParams(layoutParams2);
            dayView.setTag(i);
            TextView txtDay = dayView.findViewById(R.id.txtDay);
            TextView txtSubDay = dayView.findViewById(R.id.txtSubDay);
            View imgAvailable = dayView.findViewById(R.id.imgDayAvailable);
            RecyclerView rcyTime = dayView.findViewById(R.id.rcyTime);
            txtDay.setText(formatDayOfWeek.format(scheduleStartDate.getTime()));
            txtSubDay.setText(String.valueOf(i));
            imgAvailable.setBackgroundColor(dayClass.size() > 0
                    || today.get(Calendar.DAY_OF_YEAR) == scheduleStartDate.get(Calendar.DAY_OF_YEAR)
                    ? getColor(R.color.colorAccent) : Color.GRAY);
            ClassDataAdapter adapter = new ClassDataAdapter(dayClass);
            rcyTime.setAdapter(adapter);
            linearSchedule.addView(dayView);
            scheduleStartDate.add(Calendar.DAY_OF_WEEK, 1);
        }
    }

    @Override
    public void showQueryInterval(Calendar startDate) {
        Calendar scheduleStartDate = (Calendar) startDate.clone();
        scheduleStartDate.setTimeInMillis(scheduleStartDate.getTimeInMillis() + scheduleStartDate.getTimeZone().getRawOffset());
        SimpleDateFormat format = new SimpleDateFormat(Constants.TIME_STAMP_DAYS);
        String start = format.format(scheduleStartDate.getTime());
        scheduleStartDate.add(Calendar.DAY_OF_MONTH, Constants.SCHEDULE_INTERVAL_DAYS);
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
