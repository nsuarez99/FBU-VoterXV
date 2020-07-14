package com.example.fbu_voterxv.models;

import java.util.List;

public class User {

    public static final String TAG = "Candidate";
    private String username;
    private String email;
    private String party;
    private String image;
    private String address;
    private String state;
    private String district;
    private MyOfficials officials;
    private List<Election> electionsList;

    public User(String username, String email, String party, String image, String address, String state, String district, MyOfficials officials, List<Election> electionsList) {
        this.username = username;
        this.email = email;
        this.party = party;
        this.image = image;
        this.address = address;
        this.state = state;
        this.district = district;
        this.officials = officials;
        this.electionsList = electionsList;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getParty() {
        return party;
    }

    public String getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public MyOfficials getOfficials() {
        return officials;
    }

    public List<Election> getElectionsList() {
        return electionsList;
    }

    //    public static Candidate fromJson(JSONObject jsonObject){
//        //TODO parse election data from JSON
//    }

}
