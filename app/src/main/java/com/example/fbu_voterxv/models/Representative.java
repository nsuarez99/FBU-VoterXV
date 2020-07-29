package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

@Parcel
public class Representative extends Politician{

    public static final String TAG = "Representative";
    private String committee = "N/A";
    private String years = "N/A";
    private String state;
    private String dw_nominate;
    private String cook_pvi;

    public Representative(){}

    public String getCommittee() {
        return committee;
    }

    public String getYears() {
        return years;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public void setCommittee(String committee) {
        this.committee = committee;
    }

    public String getDw_nominate() {
        return dw_nominate;
    }

    public void setDw_nominate(String dw_nominate) {
        this.dw_nominate = dw_nominate;
    }

    public String getCook_pvi() {
        return cook_pvi;
    }

    public void setCook_pvi(String cook_pvi) {
        this.cook_pvi = cook_pvi;
    }

    @Override
    public boolean equals(Object object){
        if (object instanceof Representative){
            return sameValue((Representative) object);
        }
        else{
            return false;
        }
    }

    private boolean sameValue(Representative other){
        return other.nameEquals(name) &&  other.getState().equals(state) && other.office == office;
    }

}
