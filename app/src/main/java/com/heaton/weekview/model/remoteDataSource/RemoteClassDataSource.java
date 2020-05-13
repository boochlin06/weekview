package com.heaton.weekview.model.remoteDataSource;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heaton.weekview.constants.FormatConstants;
import com.heaton.weekview.constants.NetworkConstants;
import com.heaton.weekview.model.ClassDataSource;
import com.heaton.weekview.model.ClassInterval;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemoteClassDataSource implements ClassDataSource {

    public static final String TAG = RemoteClassDataSource.class.getSimpleName();
    private static volatile RemoteClassDataSource mInstance;
    private RemoteClassDataService.TeacherService remotePlayerDataService;

    private RemoteClassDataSource() {
        Gson gson = new GsonBuilder()
                .setDateFormat(FormatConstants.TIME_STAMP_RESPONSE_FORMAT)
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(NetworkConstants.BASE_URL)
                .build();
        remotePlayerDataService = retrofit.create(RemoteClassDataService.TeacherService.class);
    }

    public static RemoteClassDataSource getInstance() {
        if (mInstance == null) {
            synchronized (RemoteClassDataSource.class) {
                if (mInstance == null) {
                    mInstance = new RemoteClassDataSource();
                }
            }
        }
        return mInstance;
    }

    public void getScheduleList(String teacherName, String startedAt
            , GetScheduleCallback callback) {
        if (TextUtils.isEmpty(teacherName) || TextUtils.isEmpty(startedAt)) {
            Log.e(TAG, "getScheduleList onFailure: parameter is empty");
            callback.onFailure("Parameter is empty");
        } else {
            Call<ScheduleJsonObject> call = remotePlayerDataService.getScheduleList(teacherName, startedAt);
            call.enqueue(new GetScheduleListResponseCallback(callback));
        }
    }

    @Override
    public void updateScheduleList(String teacherName, List<ClassInterval> intervalList
            , boolean isBooked) {
        //Todo: try to update to api.amazing
    }


    private class GetScheduleListResponseCallback implements Callback<ScheduleJsonObject> {

        private GetScheduleCallback callback;

        public GetScheduleListResponseCallback(@NonNull GetScheduleCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<ScheduleJsonObject> call, Response<ScheduleJsonObject> response) {
            if (response.isSuccessful()) {
                ScheduleJsonObject result = response.body();
                callback.onSuccess(result);
            } else {
                String errorMessage = "";
                try {
                    errorMessage = response.errorBody().string();
                    Log.e(TAG, "GetScheduleListResponseCallback onResponse false:" + errorMessage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callback.onFailure(errorMessage);
            }
        }

        @Override
        public void onFailure(Call<ScheduleJsonObject> call, Throwable t) {
            t.printStackTrace();
            callback.onFailure(t.getMessage());
        }
    }

    public interface GetScheduleCallback {
        void onSuccess(ScheduleJsonObject scheduleJsonObject);

        void onFailure(String errorMessage);
    }
}
