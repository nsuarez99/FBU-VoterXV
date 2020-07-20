package com.example.fbu_voterxv.models;

import org.parceler.Parcel;

@Parcel
public class MyOfficials {

    public static final String TAG = "MyOfficials";
    public Representative president;
    public Representative vicePresident;
    public Representative seniorSenator;
    public Representative juniorSenator;
    public Representative congressman;

    public MyOfficials(){}


    public Representative getPresident() {
        return president;
    }

    public Representative getVicePresident() {
        return vicePresident;
    }

    public Representative getSeniorSenator() {
        return seniorSenator;
    }

    public Representative getJuniorSenator() {
        return juniorSenator;
    }

    public Representative getCongressman() {
        return congressman;
    }

    public void setPresident(Representative president) {
        this.president = president;
    }

    public void setVicePresident(Representative vicePresident) {
        this.vicePresident = vicePresident;
    }

    public void setSeniorSenator(Representative seniorSenator) {
        this.seniorSenator = seniorSenator;
    }

    public void setJuniorSenator(Representative juniorSenator) {
        this.juniorSenator = juniorSenator;
    }

    public void setCongressman(Representative congressman) {
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
