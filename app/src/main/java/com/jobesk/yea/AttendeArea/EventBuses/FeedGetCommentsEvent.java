package com.jobesk.yea.AttendeArea.EventBuses;

public class FeedGetCommentsEvent {

    int pos;
    int postID;

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public FeedGetCommentsEvent(int pos, int postID) {
        this.pos = pos;
        this.postID = postID;
    }

    public FeedGetCommentsEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
