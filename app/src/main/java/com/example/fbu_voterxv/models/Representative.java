package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

@Parcel
public class Representative extends Politician{

    public static final String TAG = "Representative";
    private String committee = "N/A";
    private String years = "N/A";

    public Representative(){}

    public String getCommittee() {
        return committee;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

}
