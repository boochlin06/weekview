package com.heaton.weekview.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "classInterval")
public class ClassInterval {
    private String start;
    private String end;

    @PrimaryKey(autoGenerate = true)
    private long id;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
