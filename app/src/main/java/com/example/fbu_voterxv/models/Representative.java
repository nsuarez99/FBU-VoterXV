package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

@Parcel
public class Representative extends Politician{

    public static final String TAG = "Representative";
    private String committee = "N/A";
    private String years = "N/A";

    public Representative(){}

    public Representative(String name, int age, String profileImage, String party, String gender, String website, String fb, String twitter, String office, String committee, String years) {
        super(name, age, profileImage, party, gender, website, fb, twitter, office);
        this.committee = committee;
        this.years = years;
    }

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
