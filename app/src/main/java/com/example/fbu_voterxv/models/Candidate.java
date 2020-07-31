package com.example.fbu_voterxv.models;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Parcel
public class Candidate extends Politician{

    public static final String TAG = "Candidate";
    private long money_raised;
    private String slogan;
    private String incumbentStatus;

    public Candidate(){}

    public long getMoney_raised() {
        return money_raised;
    }

    public String getMoney_raisedString() {
        String simple = NumberFormat.getInstance(Locale.US).format(money_raised);
        return "$" + simple;
    }

    public String getSlogan() {
        return slogan;
    }

    public String getIncumbentStatus() {
        return incumbentStatus;
    }

    public void setIncumbentStatus(String incumbentStatus) {
        this.incumbentStatus = incumbentStatus;
    }

    public void setMoney_raised(long money_raised) {
        this.money_raised = money_raised;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

}
