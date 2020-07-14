package com.example.fbu_voterxv.models;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Election {

    public static final String TAG = "Election";
    private String title;
    private String date;
    private List<Candidate> candidates;

    public Election(){}

    public Election(String title, String date, List<Candidate> candidates) {
        this.title = title;
        this.date = date;
        this.candidates = candidates;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

//    public static Election fromJson(JSONObject jsonObject){
//        //TODO parse election data from JSON
//    }
}
