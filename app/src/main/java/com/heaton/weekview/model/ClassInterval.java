package com.heaton.weekview.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "classInterval")
public class ClassInterval {
    @SerializedName("start")
    @TypeConverters({DateTypeConverters.class})
    private Date startAt;

    @SerializedName("end")
    @TypeConverters({DateTypeConverters.class})
    private Date endAt;
    private boolean isBooked;
    private String teacherName;

    @PrimaryKey
    private long id;

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public long getId() {
        return startAt.getTime();
    }

    public void setId(long id) {
        this.id = startAt.getTime();
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
