package com.example.fbu_voterxv.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;

@Parcel
public class User {

    public static final String TAG = "User";
    private int age;
    private String username;
    private String email;
    private String party;
    private ParseFile image;
    private String street;
    private String city;
    private String zipcode;
    private String state;
    private String district;
    private MyOfficials officials;
    private List<Election> electionsList;

    public User(){}

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public int getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getParty() {
        return party;
    }

    public ParseFile getImage() {
        return image;
    }

    public String getStreet() {
        return street;
    }

    public String getState() {
        return state;
    }

    public String getDistrict() {
        return district;
    }

    public MyOfficials getOfficials() {
        return officials;
    }

    public List<Election> getElectionsList() {
        return electionsList;
    }

    public String getAddress() {
        return String.format("%s, %s, %s, %s", street, city, state, zipcode);
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public void setImage(ParseFile image) {
        this.image = image;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setOfficials(MyOfficials officials) {
        this.officials = officials;
    }

    public void setElectionsList(List<Election> electionsList) {
        this.electionsList = electionsList;
    }

}
