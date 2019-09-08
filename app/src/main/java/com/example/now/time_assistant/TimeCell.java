package com.example.now.time_assistant;

public class TimeCell {

    Integer StartTime;
    Integer EndTime;

    public TimeCell(Integer startTime, Integer endTime) {
        StartTime = startTime;
        EndTime = endTime;
    }

    public Integer getStartTime() {
        return StartTime;
    }

    public void setStartTime(Integer startTime) {
        StartTime = startTime;
    }

    public Integer getEndTime() {
        return EndTime;
    }

    public void setEndTime(Integer endTime) {
        EndTime = endTime;
    }
}
