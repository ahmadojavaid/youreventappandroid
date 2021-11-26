package com.jobesk.yea.AttendeArea.EventBuses;

public class LeaderBoardEventBus {


    String keyword;

    public LeaderBoardEventBus(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
