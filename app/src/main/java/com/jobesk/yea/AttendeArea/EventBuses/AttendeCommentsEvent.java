package com.jobesk.yea.AttendeArea.EventBuses;

public class AttendeCommentsEvent {


    int pos;
    int postID;

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public AttendeCommentsEvent(int pos, int postID) {
        this.pos = pos;
        this.postID = postID;
    }

    public AttendeCommentsEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
