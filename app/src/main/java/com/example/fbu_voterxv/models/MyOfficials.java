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

    public static MyOfficials fromJsonObject(JSONObject jsonObject) throws JSONException {
        MyOfficials myOfficials = new MyOfficials();
        JSONArray officesArray = jsonObject.getJSONArray("officials");
        for (int i = 0; i < officesArray.length() ; i++) {
            JSONObject official = officesArray.getJSONObject(i);

            Representative politician = new Representative();

            politician.setName(official.getString("name"));
            politician.setParty(official.getString("party"));
            politician.setWebsite(official.getJSONArray("urls").getString(0));

            //set photoURL if available
            if (official.has("photoUrl")){
                politician.setProfileImage(official.getString("photoUrl"));
            }
            else{
                politician.setProfileImage("N/A");
                Log.i(TAG, politician.name + " no photoUrl available");
            }

            //set social media accounts if available
            JSONArray channels = official.getJSONArray("channels");
            politician.setFb("N/A");
            politician.setTwitter("N/A");
            for (int j = 0; j < channels.length() ; j++) {
                JSONObject channel = channels.getJSONObject(j);
                if (channel.getString("type").equals("Facebook")){
                    politician.setFb(channel.getString("id"));
                }
                else if (channel.getString("type").equals("Twitter")){
                    politician.setTwitter(channel.getString("id"));
                }
            }

            //set politician as official
            if (i == 0){
                myOfficials.setPresident(politician);
            }
            else if (i == 1){
                myOfficials.setVicePresident(politician);
            }
            else if (i == 2){
                myOfficials.setSeniorSenator(politician);
            }
            else if (i == 3){
                myOfficials.setJuniorSenator(politician);
            }
            else if (i == 4){
                myOfficials.setCongressman(politician);
            }
        }
        return myOfficials;
    }
}
