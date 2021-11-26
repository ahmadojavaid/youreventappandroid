package com.jobesk.yea.AttendeArea.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class AgendaModel implements Serializable {


    ArrayList<AgendaSponsorModel> sponsersList;
    ArrayList<AgendaSpeakerModel> speakserList;
    ArrayList<AgendaDocModel> doclist;

    String sessionID, sessionName, sessionVenue, date, timeFrom, timeTo;
    String sessionDesc;


    public String getSessionDesc() {
        return sessionDesc;
    }

    public void setSessionDesc(String sessionDesc) {
        this.sessionDesc = sessionDesc;
    }

    public ArrayList<AgendaSponsorModel> getSponsersList() {
        return sponsersList;
    }

    public void setSponsersList(ArrayList<AgendaSponsorModel> sponsersList) {
        this.sponsersList = sponsersList;
    }

    public ArrayList<AgendaSpeakerModel> getSpeakserList() {
        return speakserList;
    }

    public void setSpeakserList(ArrayList<AgendaSpeakerModel> speakserList) {
        this.speakserList = speakserList;
    }

    public ArrayList<AgendaDocModel> getDoclist() {
        return doclist;
    }

    public void setDoclist(ArrayList<AgendaDocModel> doclist) {
        this.doclist = doclist;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionVenue() {
        return sessionVenue;
    }

    public void setSessionVenue(String sessionVenue) {
        this.sessionVenue = sessionVenue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }
}
