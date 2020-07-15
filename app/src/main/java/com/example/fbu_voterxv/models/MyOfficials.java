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

    public MyOfficials(Representative president, Representative vicePresident, Representative seniorSenator, Representative juniorSenator, Representative congressman) {
        this.president = president;
        this.vicePresident = vicePresident;
        this.seniorSenator = seniorSenator;
        this.juniorSenator = juniorSenator;
        this.congressman = congressman;
    }

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
}
