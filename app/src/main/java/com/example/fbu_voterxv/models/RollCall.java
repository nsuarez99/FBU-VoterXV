package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Parcel
public class RollCall {

    public static final String TAG = "RollCall";
    private Map<String, Integer> democratBreakdown;
    private Map<String, Integer> republicanBreakdown;
    private Map<String, Integer> independentBreakdown;
    private Map<String, Integer> totalBreakdown;
    private Date date;
    private String result;

    private Map<Representative, String> votes;

    public RollCall(){}

    public Map<String, Integer> getDemocratBreakdown() {
        return democratBreakdown;
    }

    public void setDemocratBreakdown(Map<String, Integer> democratBreakdown) {
        this.democratBreakdown = democratBreakdown;
    }

    public Map<String, Integer> getRepublicanBreakdown() {
        return republicanBreakdown;
    }

    public void setRepublicanBreakdown(Map<String, Integer> republicanBreakdown) {
        this.republicanBreakdown = republicanBreakdown;
    }

    public Map<String, Integer> getIndependentBreakdown() {
        return independentBreakdown;
    }

    public void setIndependentBreakdown(Map<String, Integer> independentBreakdown) {
        this.independentBreakdown = independentBreakdown;
    }

    public Map<Representative, String> getVotes() {
        return votes;
    }

    public void setVotes(Map<Representative, String> votes) {
        this.votes = votes;
    }

    public Map<String, Integer> getTotalBreakdown() {
        return totalBreakdown;
    }

    public void setTotalBreakdown(Map<String, Integer> totalBreakdown) {
        this.totalBreakdown = totalBreakdown;
    }

    public void addVote(Representative representative, String vote){
        votes.put(representative, vote);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
