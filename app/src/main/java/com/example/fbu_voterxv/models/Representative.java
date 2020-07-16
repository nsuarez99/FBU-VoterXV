package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

@Parcel
public class Representative extends Politician{

    public static final String TAG = "Representative";
    private String committee = "N/A";

    public Representative(){}

    public Representative(String name, int age, String profileImage, String party, String gender, String website, String fb, String twitter, String committee) {
        super(name, age, profileImage, party, gender, website, fb, twitter);
        this.committee = committee;
    }

    public String getCommittee() {
        return committee;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

}
