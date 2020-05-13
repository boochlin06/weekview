package com.heaton.weekview.model.localDataSource;

import androidx.room.Dao;
import androidx.room.Query;

import com.heaton.weekview.model.ClassInterval;

import java.util.List;

@Dao
public abstract class ClassIntervalDao extends BaseDao<ClassInterval> {
    // TODO: set more than start and less than end.
//    @Query("SELECT * from ClassInterval WHERE teacherName=:teacherName AND startAt>=:startAt AND endAt <=:endAt AND isBooked=:isBooked")
//    abstract public List<ClassInterval> getClassIntervals(String teacherName, String startAt
//            , String endAt , boolean isBooked);
    @Query("SELECT * from ClassInterval WHERE teacherName=:teacherName AND isBooked=:isBooked")
    abstract public List<ClassInterval> getClassIntervals(String teacherName, boolean isBooked);
}
