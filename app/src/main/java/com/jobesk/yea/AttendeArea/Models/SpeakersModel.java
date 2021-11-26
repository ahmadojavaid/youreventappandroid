package com.jobesk.yea.AttendeArea.Models;

public class SpeakersModel {

    public static final int TYEPE_SEARCH = 1;
    public static final int TYPE_USER = 2;



    int type;
    String id, speakerName, speakerOccupation, speakerCompanyName, speakerDetails, speakerProfileImage;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public String getSpeakerOccupation() {
        return speakerOccupation;
    }

    public void setSpeakerOccupation(String speakerOccupation) {
        this.speakerOccupation = speakerOccupation;
    }

    public String getSpeakerCompanyName() {
        return speakerCompanyName;
    }

    public void setSpeakerCompanyName(String speakerCompanyName) {
        this.speakerCompanyName = speakerCompanyName;
    }

    public String getSpeakerDetails() {
        return speakerDetails;
    }

    public void setSpeakerDetails(String speakerDetails) {
        this.speakerDetails = speakerDetails;
    }

    public String getSpeakerProfileImage() {
        return speakerProfileImage;
    }

    public void setSpeakerProfileImage(String speakerProfileImage) {
        this.speakerProfileImage = speakerProfileImage;
    }
}
