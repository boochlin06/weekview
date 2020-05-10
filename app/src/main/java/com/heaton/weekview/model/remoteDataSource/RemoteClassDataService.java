package com.heaton.weekview.model.remoteDataSource;

import com.heaton.weekview.model.ClassInterval;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RemoteClassDataService {
    public static final String BASE_URL = "https://api.amazingtalker.com/v1/guest/teachers/amy-estrada/schedule";
    //?started_at=2020-05-09T16%253A00%253A00.000Z

    public interface TeacherService {
        @GET("v1/guest/teachers/{teacherName}/")
        Call<List<ClassInterval>> getScheduleList(@Path("teacherName") String teacherName
                , @Query("started_at") String startedAt);
    }
}
