package com.jobesk.yea.AttendeArea.Models;


import java.util.ArrayList;

public class FeedModel {


//    public static final int CREATE_POST_TYPE = 0;
    public static final int USER_INFO_TYPE = 1;
    public static final int VIDEO_TYPE = 2;
        public static final int TEXT_TYPE = 3;
    public static final int EVENT_TYPE = 4;
    public static final int SINGLE_IMAGE_TYPE = 5;
    public static final int DOUBLE_IMAGE_TYPE = 6;
    public static final int TRIPLE_IMAGE_TYPE = 7;
    public static final int FOUR_IMAGE_TYPE = 8;
    public static final int MULTI_IMAGE_TYPE = 9;


    //    public static final int AUDIO_TYPE = 2;


    int type;

    String isPined;

    String postID, userId,name,image, postDescription, postLikesCount, postCommentsCount, postSharesCount, postMediaType,

    date;

    ArrayList<CommentsModel> commentsList;

    ArrayList<String> postattachments;


    int commentListSize;



    String postTotalImages,isLiked;

    public String getIsPined() {
        return isPined;
    }

    public void setIsPined(String isPined) {
        this.isPined = isPined;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCommentListSize() {
        return commentListSize;
    }

    public void setCommentListSize(int commentListSize) {
        this.commentListSize = commentListSize;
    }

    public String getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(String isLiked) {
        this.isLiked = isLiked;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPostID() {
        return postID;
    }

    public String getPostTotalImages() {
        return postTotalImages;
    }

    public void setPostTotalImages(String postTotalImages) {
        this.postTotalImages = postTotalImages;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostLikesCount() {
        return postLikesCount;
    }

    public void setPostLikesCount(String postLikesCount) {
        this.postLikesCount = postLikesCount;
    }

    public String getPostCommentsCount() {
        return postCommentsCount;
    }

    public void setPostCommentsCount(String postCommentsCount) {
        this.postCommentsCount = postCommentsCount;
    }

    public String getPostSharesCount() {
        return postSharesCount;
    }

    public void setPostSharesCount(String postSharesCount) {
        this.postSharesCount = postSharesCount;
    }

    public String getPostMediaType() {
        return postMediaType;
    }

    public void setPostMediaType(String postMediaType) {
        this.postMediaType = postMediaType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<CommentsModel> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(ArrayList<CommentsModel> commentsList) {
        this.commentsList = commentsList;
    }

    public ArrayList<String> getPostattachments() {
        return postattachments;
    }

    public void setPostattachments(ArrayList<String> postattachments) {
        this.postattachments = postattachments;
    }


}

