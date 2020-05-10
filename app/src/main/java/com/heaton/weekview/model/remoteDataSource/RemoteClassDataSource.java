package com.heaton.weekview.model.remoteDataSource;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.heaton.weekview.Utility;
import com.heaton.weekview.model.ClassData;
import com.heaton.weekview.model.ClassDataSource;
import com.heaton.weekview.model.ClassInterval;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RemoteClassDataSource implements ClassDataSource {

    public static final String TAG = RemoteClassDataSource.class.getSimpleName();
    private static volatile RemoteClassDataSource mInstance;
    private RemoteClassDataService.TeacherService remotePlayerDataService;

    private RemoteClassDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(RemoteClassDataService.BASE_URL)
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
            , GetListCallback callback) {
        if (TextUtils.isEmpty(teacherName) || TextUtils.isEmpty(startedAt)) {
            Log.e(TAG, "getScheduleList onFailure: parameter is empty");
            callback.onFailure("Parameter is empty");
        } else {
            Call<List<ClassInterval>> call = remotePlayerDataService.getScheduleList(teacherName, startedAt);
            call.enqueue(new GetScheduleListResponseCallback(callback));
        }
    }

    @Override
    public void updateScheduleList(String teacherName, List<ClassInterval> intervalList) {
        //Nothing to do.
    }


    private class GetScheduleListResponseCallback implements Callback<ScheduleJsonObject> {

        private GetListCallback callback;

        public GetScheduleListResponseCallback(@NonNull GetListCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<ScheduleJsonObject> call, Response<ScheduleJsonObject> response) {
            if (response.isSuccessful()) {
                ScheduleJsonObject result = response.body();
                Map<Integer, List<ClassData>> map = Utility.convertScheduleDataToDayList(result);

                callback.onSuccess(response.body());
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

    private interface GetScheduleCallback {

    }

    public interface GetListCallback {
        void onSuccess(List<ClassInterval> intervalList);

        void onFailure(String errorMessage);
    }
}
