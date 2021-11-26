package com.jobesk.yea.AttendeArea.Models;

public class LeaderBoardModel {

    public static final int TYEPE_SEARCH = 1;
    public static final int TYPE_USER = 2;


    String isFollow;
    String companyName,jobTitle;

    int type;

    String points;


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(String isFollow) {
        this.isFollow = isFollow;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    String name,id,image,follwingCount,follersCount;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFollwingCount() {
        return follwingCount;
    }

    public void setFollwingCount(String follwingCount) {
        this.follwingCount = follwingCount;
    }

    public String getFollersCount() {
        return follersCount;
    }

    public void setFollersCount(String follersCount) {
        this.follersCount = follersCount;
    }
}
