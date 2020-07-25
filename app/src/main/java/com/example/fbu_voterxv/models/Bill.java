package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Parcel
public class Bill{

    public static final String TAG = "Bill";

    private String slugID;
    private String congress;
    private String title;
    private String briefSummary;
    private String summary;
    private Representative sponsor;
    private Map<String, Integer> cosponsors;
    private String subject;
    private Date lastAction;
    private String url;
    private Boolean house_vote;
    private Boolean senate_vote;
    private Boolean veto;
    private RollCall houseRollCall;
    private RollCall senateRollCall;

    public Bill(){}

    public String getSlugID() {
        return slugID;
    }

    public String getCongress() {
        return congress;
    }

    public String getTitle() {
        return title;
    }

    public String getBriefSummary() {
        return briefSummary;
    }

    public String getSummary() {
        return summary;
    }

    public Representative getSponsor() {
        return sponsor;
    }

    public Map<String, Integer> getCosponsors() {
        return cosponsors;
    }

    public String getSubject() {
        return subject;
    }

    public Date getLastAction() {
        return lastAction;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getHouse_vote() {
        return house_vote;
    }

    public Boolean getSenate_vote() {
        return senate_vote;
    }

    public Boolean getVeto() {
        return veto;
    }

    public RollCall getHouseRollCall() {
        return houseRollCall;
    }

    public void setHouseRollCall(RollCall houseRollCall) {
        this.houseRollCall = houseRollCall;
    }

    public RollCall getSenateRollCall() {
        return senateRollCall;
    }

    public void setSenateRollCall(RollCall senateRollCall) {
        this.senateRollCall = senateRollCall;
    }

    public void setHouse_vote(Boolean house_vote) {
        this.house_vote = house_vote;
    }

    public void setSenate_vote(Boolean senate_vote) {
        this.senate_vote = senate_vote;
    }

    public void setVeto(Boolean veto) {
        this.veto = veto;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLastAction(Date lastAction) {
        this.lastAction = lastAction;
    }

    public void setSlugID(String slugID) {
        this.slugID = slugID;
    }

    public void setCongress(String congress) {
        this.congress = congress;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBriefSummary(String briefSummary) {
        this.briefSummary = briefSummary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSponsor(Representative sponsor) {
        this.sponsor = sponsor;
    }

    public void setCosponsors(Map<String, Integer> cosponsors) {
        this.cosponsors = cosponsors;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
