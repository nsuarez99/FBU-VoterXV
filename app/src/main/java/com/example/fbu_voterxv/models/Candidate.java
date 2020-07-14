package com.example.fbu_voterxv.models;

import org.json.JSONObject;

import java.util.List;

public class Candidate {

    public static final String TAG = "Candidate";
    private String name;
    private int age;
    private String profileImage;
    private String party;
    private String gender;
    private String website;
    private String money_raised;
    private String fb;
    private String twitter;

    public Candidate(String name, int age, String profileImage, String party, String gender, String website, String money_raised, String fb, String twitter) {
        this.name = name;
        this.age = age;
        this.profileImage = profileImage;
        this.party = party;
        this.gender = gender;
        this.website = website;
        this.money_raised = money_raised;
        this.fb = fb;
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getParty() {
        return party;
    }

    public String getGender() {
        return gender;
    }

    public String getWebsite() {
        return website;
    }

    public String getMoney_raised() {
        return money_raised;
    }

    public String getFb() {
        return fb;
    }

    public String getTwitter() {
        return twitter;
    }

    //    public static Candidate fromJson(JSONObject jsonObject){
//        //TODO parse election data from JSON
//    }

}
