package com.jobesk.yea.AttendeArea.EventBuses;

public class SpeakersEventBus {


    String keyword;

    public SpeakersEventBus(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
