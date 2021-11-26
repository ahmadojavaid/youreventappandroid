package com.jobesk.yea.EventBuses;

public class AttendeDateTitleEvent {

    String Date;

    public AttendeDateTitleEvent(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
