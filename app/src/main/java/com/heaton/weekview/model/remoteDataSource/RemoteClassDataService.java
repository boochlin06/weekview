package com.heaton.weekview.model.remoteDataSource;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RemoteClassDataService {
    public static final String BASE_URL = "https://api.amazingtalker.com";
    //?started_at=2020-05-09T16

    public interface TeacherService {
        @GET("v1/guest/teachers/{teacherName}/schedule")
        Call<ScheduleJsonObject> getScheduleList(@Path("teacherName") String teacherName
                , @Query("started_at") String startedAt);
    }
}
