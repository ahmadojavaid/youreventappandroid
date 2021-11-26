package com.jobesk.yea.EventBuses;

public class SponsorDateEventBus {

    String Date;

    public SponsorDateEventBus(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
