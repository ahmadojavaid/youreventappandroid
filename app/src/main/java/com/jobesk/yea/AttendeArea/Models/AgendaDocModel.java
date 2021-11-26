package com.jobesk.yea.AttendeArea.Models;

import java.io.Serializable;

public class AgendaDocModel implements Serializable {

    String docId,speakerId,DocattachementURl,created_at,docname;


    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(String speakerId) {
        this.speakerId = speakerId;
    }

    public String getDocattachementURl() {
        return DocattachementURl;
    }

    public void setDocattachementURl(String docattachementURl) {
        DocattachementURl = docattachementURl;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
