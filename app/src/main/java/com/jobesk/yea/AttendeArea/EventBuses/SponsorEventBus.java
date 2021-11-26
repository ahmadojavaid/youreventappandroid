package com.jobesk.yea.AttendeArea.EventBuses;

public class SponsorEventBus {


    String keyword;

    public SponsorEventBus(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
