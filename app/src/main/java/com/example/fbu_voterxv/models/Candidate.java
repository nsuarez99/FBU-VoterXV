package com.example.fbu_voterxv.models;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Candidate extends Politician{

    public static final String TAG = "Candidate";
    private long money_raised;
    private String slogan;

    public Candidate(){}

    public long getMoney_raised() {
        return money_raised;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setMoney_raised(long money_raised) {
        this.money_raised = money_raised;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    //        public static Candidate fromJson(JSONObject jsonObject){
//        //TODO parse election data from JSON
//    }

}
