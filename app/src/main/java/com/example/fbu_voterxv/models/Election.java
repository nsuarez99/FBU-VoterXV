package com.example.fbu_voterxv.models;

import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Parcel
public class Election {

    public static final String TAG = "Election";
    private String name;
    private Date date;
    private List<Candidate> candidates;
    private Offices office;

    public Election(){}

    public Election(String name, Date date, List<Candidate> candidates) {
        this.name = name;
        this.date = date;
        this.candidates = candidates;
    }

    public List<Candidate> getCandidates() {
        return candidates;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date);
    }

    public String getYear(){
        String date = getDateString();
        return date.substring(6);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCandidates(List<Candidate> candidates) {
        this.candidates = candidates;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Election){
            return sameValue((Election) object);
        }
        else{
            return false;
        }
    }

    private boolean sameValue(Election other){
        return other.name.toLowerCase().equals(name.toLowerCase()) &&  other.date.equals(date) && other.office == office;
    }

    public Offices getOffice() {
        return office;
    }

    public void setOffice(Offices office) {
        this.office = office;
    }
}
