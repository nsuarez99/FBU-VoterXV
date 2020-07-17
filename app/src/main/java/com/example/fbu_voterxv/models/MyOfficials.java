package com.example.fbu_voterxv.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class MyOfficials {

    public static final String TAG = "MyOfficials";
    public Politician president;
    public Politician vicePresident;
    public Politician seniorSenator;
    public Politician juniorSenator;
    public Politician congressman;

    public MyOfficials(){}

    public MyOfficials(Politician president, Politician vicePresident, Politician seniorSenator, Politician juniorSenator, Politician congressman) {
        this.president = president;
        this.vicePresident = vicePresident;
        this.seniorSenator = seniorSenator;
        this.juniorSenator = juniorSenator;
        this.congressman = congressman;
    }

    public Politician getPresident() {
        return president;
    }

    public Politician getVicePresident() {
        return vicePresident;
    }

    public Politician getSeniorSenator() {
        return seniorSenator;
    }

    public Politician getJuniorSenator() {
        return juniorSenator;
    }

    public Politician getCongressman() {
        return congressman;
    }

    public void setPresident(Politician president) {
        this.president = president;
    }

    public void setVicePresident(Politician vicePresident) {
        this.vicePresident = vicePresident;
    }

    public void setSeniorSenator(Politician seniorSenator) {
        this.seniorSenator = seniorSenator;
    }

    public void setJuniorSenator(Politician juniorSenator) {
        this.juniorSenator = juniorSenator;
    }

    public void setCongressman(Politician congressman) {
        this.congressman = congressman;
    }

    @Override
    public String toString(){
        String result;
        try{
            result = String.format("President: %s, VicePresdient: %s, SeniorSenator: %s, JuniorSenator: %s, Congressman: %s",
                    president.name, vicePresident.name, seniorSenator.name, juniorSenator.name, congressman.name);
        }
        catch (NullPointerException e){
            result = "Cannot return string as not all politicians are set";
        }
        return result;
    }

}
