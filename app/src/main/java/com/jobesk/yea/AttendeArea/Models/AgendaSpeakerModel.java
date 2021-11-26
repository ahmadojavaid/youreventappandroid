package com.jobesk.yea.AttendeArea.Models;

import java.io.Serializable;

public class AgendaSpeakerModel  implements Serializable {

    String sponserID,sessionId,speakerId,created_at,speakerName,speakerOccupation,speakerCompanyName,speakerDetails,speakerProfileImage;

    public String getSponserID() {
        return sponserID;
    }

    public void setSponserID(String sponserID) {
        this.sponserID = sponserID;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
