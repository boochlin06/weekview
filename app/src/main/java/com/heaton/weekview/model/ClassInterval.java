package com.heaton.weekview.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "classInterval")
public class ClassInterval {
    @SerializedName("start")
    @Ignore
//    @TypeConverters({Converters.class})
    private Date startAt;
    @SerializedName("end")
//    @TypeConverters({Converters.class})
    @Ignore
    private Date endAt;
    private boolean isBooked;
    private String teacherName;

    @PrimaryKey(autoGenerate = true)
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
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
