package com.heaton.weekview.model;

import com.heaton.weekview.model.remoteDataSource.RemoteClassDataSource;

import java.util.List;

public interface ClassDataSource {
    void getScheduleList(String teacherName, String startedAt
            , RemoteClassDataSource.GetListCallback callback);

    void updateScheduleList(String teacherName, List<ClassInterval> intervalList);
}
