package com.example.fbu_voterxv.models;

import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Politician {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}
