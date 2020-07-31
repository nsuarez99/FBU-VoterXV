package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

import java.util.Date;
import java.util.Map;

@Parcel
public class PoliticalView {

    public static final String TAG = "Beliefs";

    private String subject;
    private String summary;

    public PoliticalView() {}

    public PoliticalView(String subject, String summary) {
        this.subject = subject;
        this.summary = summary;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
