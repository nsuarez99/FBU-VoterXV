package com.example.fbu_voterxv.models;

import org.json.JSONObject;
import org.parceler.Parcel;

public abstract class Politician {

    public static final String TAG = "Representative";
    String name;
    int age;
    String profileImage;
    String party;
    String gender;
    String website;
    String fb;
    String twitter;

    public Politician(String name, int age, String profileImage, String party, String gender, String website, String fb, String twitter) {
        this.name = name;
        this.age = age;
        this.profileImage = profileImage;
        this.party = party;
        this.gender = gender;
        this.website = website;
        this.fb = fb;
        this.twitter = twitter;
    }

    public Politician() {}

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

    public String getFb() {
        return fb;
    }

    public String getTwitter() {
        return twitter;
    }

}
