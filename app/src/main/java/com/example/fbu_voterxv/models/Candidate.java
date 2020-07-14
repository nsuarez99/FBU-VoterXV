package com.example.fbu_voterxv.models;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Candidate extends Politician{

    public static final String TAG = "Candidate";
    private String money_raised;

    public Candidate(String name, int age, String profileImage, String party, String gender, String website, String fb, String twitter, String money_raised) {
        super(name, age, profileImage, party, gender, website, fb, twitter);
        this.money_raised = money_raised;
    }

    public Candidate(){}

    public String getMoney_raised() {
        return money_raised;
    }


    //    public static Candidate fromJson(JSONObject jsonObject){
//        //TODO parse election data from JSON
//    }

}
