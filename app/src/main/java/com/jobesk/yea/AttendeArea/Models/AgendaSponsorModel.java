package com.jobesk.yea.AttendeArea.Models;

import java.io.Serializable;

public class AgendaSponsorModel implements Serializable {

    String sponsorID,sessionId,created_at,sponsorName,sponsorImage,sponsorDescription,sponsorTitle,sponsorshipLevel,sponsorwebLink;
String speakerID;


    public String getSpeakerID() {
        return speakerID;
    }

    public void setSpeakerID(String speakerID) {
        this.speakerID = speakerID;
    }

    public String getSponsorID() {
        return sponsorID;
    }

    public void setSponsorID(String sponsorID) {
        this.sponsorID = sponsorID;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorImage() {
        return sponsorImage;
    }

    public void setSponsorImage(String sponsorImage) {
        this.sponsorImage = sponsorImage;
    }

    public String getSponsorDescription() {
        return sponsorDescription;
    }

    public void setSponsorDescription(String sponsorDescription) {
        this.sponsorDescription = sponsorDescription;
    }

    public String getSponsorTitle() {
        return sponsorTitle;
    }

    public void setSponsorTitle(String sponsorTitle) {
        this.sponsorTitle = sponsorTitle;
    }

    public String getSponsorshipLevel() {
        return sponsorshipLevel;
    }

    public void setSponsorshipLevel(String sponsorshipLevel) {
        this.sponsorshipLevel = sponsorshipLevel;
    }

    public String getSponsorwebLink() {
        return sponsorwebLink;
    }

    public void setSponsorwebLink(String sponsorwebLink) {
        this.sponsorwebLink = sponsorwebLink;
    }
}
