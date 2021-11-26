package com.jobesk.yea.AttendeArea.Models;

public class AAvaliableModel {


    String session_id, from, to, time_id,sponsor_msg,attendee_msg,status,name,image;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSponsor_msg() {
        return sponsor_msg;
    }

    public void setSponsor_msg(String sponsor_msg) {
        this.sponsor_msg = sponsor_msg;
    }

    public String getAttendee_msg() {
        return attendee_msg;
    }

    public void setAttendee_msg(String attendee_msg) {
        this.attendee_msg = attendee_msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
    }
}
