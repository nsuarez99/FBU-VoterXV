package com.example.fbu_voterxv.models;

public class Representative {

    public static final String TAG = "Representative";
    private String name;
    private int age;
    private String profileImage;
    private String party;
    private String gender;
    private String website;
    private String committee;
    private String fb;
    private String twitter;

    public Representative(String name, int age, String profileImage, String party, String gender, String website, String money_raised, String fb, String twitter) {
        this.name = name;
        this.age = age;
        this.profileImage = profileImage;
        this.party = party;
        this.gender = gender;
        this.website = website;
        this.committee = committee;
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

    public String getCommittee() {
        return committee;
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
