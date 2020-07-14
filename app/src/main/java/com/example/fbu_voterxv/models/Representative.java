package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

@Parcel
public class Representative extends Politician{

    public static final String TAG = "Representative";
    private String committee;

    public Representative(){}

    public Representative(String name, int age, String profileImage, String party, String gender, String website, String fb, String twitter, String committee) {
        super(name, age, profileImage, party, gender, website, fb, twitter);
        this.committee = committee;
    }

    public String getCommittee() {
        return committee;
    }

    //    public static Representative fromJson(JSONObject jsonObject){
//        //TODO parse election data from JSON
//    }

}
