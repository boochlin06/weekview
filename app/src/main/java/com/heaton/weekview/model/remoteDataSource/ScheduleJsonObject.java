package com.heaton.weekview.model.remoteDataSource;

import com.google.gson.annotations.SerializedName;
import com.heaton.weekview.model.ClassInterval;

import java.util.List;

public class ScheduleJsonObject {

    @SerializedName("available")
    private List<ClassInterval> availableList;
    @SerializedName("booked")
    private List<ClassInterval> bookedList;

    public List<ClassInterval> getAvailableList() {
        return availableList;
    }

    public void setAvailableList(List<ClassInterval> availableList) {
        this.availableList = availableList;
    }

    public List<ClassInterval> getBookedList() {
        return bookedList;
    }

    public void setBookedList(List<ClassInterval> bookedList) {
        this.bookedList = bookedList;
    }
}
