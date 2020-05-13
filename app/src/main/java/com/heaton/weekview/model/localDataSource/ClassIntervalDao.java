package com.heaton.weekview.model.localDataSource;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.heaton.weekview.model.ClassInterval;

import java.util.Date;
import java.util.List;

@Dao
public abstract class ClassIntervalDao extends BaseDao<ClassInterval> {
    @Query("SELECT * from ClassInterval WHERE teacherName=:teacherName AND startAt>=:startAt AND endAt <=:endAt AND isBooked=:isBooked")
    abstract public List<ClassInterval> getClassIntervals(String teacherName, Date startAt
            , Date endAt, boolean isBooked);

    @Query("SELECT * from ClassInterval WHERE teacherName=:teacherName AND isBooked=:isBooked")
    abstract public List<ClassInterval> getClassIntervals(String teacherName, boolean isBooked);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract public void insertAll(List<ClassInterval> classIntervalList);
}
