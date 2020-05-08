package com.heaton.weekview.model.localDataSource;

import androidx.room.Dao;
import androidx.room.Query;

import com.heaton.weekview.model.ClassInterval;

import java.util.List;

@Dao
public abstract class ClassIntervalDao extends BaseDao<ClassInterval> {
    @Query("SELECT * from ClassInterval")
    abstract public List<ClassInterval> getClassIntervals();
}
