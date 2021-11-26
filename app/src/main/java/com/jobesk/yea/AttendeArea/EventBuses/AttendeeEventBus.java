package com.jobesk.yea.AttendeArea.EventBuses;

public class AttendeeEventBus {


    String keyword;

    public AttendeeEventBus(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
