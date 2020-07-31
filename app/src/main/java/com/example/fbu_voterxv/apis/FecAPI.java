package com.example.fbu_voterxv.apis;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_voterxv.BuildConfig;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.fragments.OfficialsFragment;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class FecAPI {

    public static final String TAG = "FecAPI";
    private static final String BASE_URL = "https://api.open.fec.gov/v1/";
    private static final String KEY = BuildConfig.FEC_KEY;

    protected static String getFecOfficeString(Offices office){
        if (office == Offices.HOUSE_OF_REPRESENTATIVES){
            return "H";
        }
        else if (office == Offices.SENATE){
            return "S";
        }
        else{
            return "P";
        }
    }

    protected static Offices parseOffice(String office){
        if (office.equals("H")){
            return Offices.HOUSE_OF_REPRESENTATIVES;
        }
        else if (office.equals("S")){
            return Offices.SENATE;
        }
        else{
            return Offices.PRESIDENT;
        }
    }

    public static class CandidateInfoParse{

        public static void setCandidatesInfo(final User user){
            List<Election> elections = user.getElectionsList();
            String state = user.getState();
            for (Election election : elections){
                List<Candidate> candidates = election.getCandidates();
                setCandidatesBasic(candidates, state);
                setCandidatesMoney(candidates, state, election.getYear());
            }
        }

        //TODO do different parse if president
        //sets candidates money raised
        public static void setCandidatesMoney(final List<Candidate> candidates, String state, String year) {
            final String URL = BASE_URL + "candidates/totals/";
            for (final Candidate candidate : candidates){
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("api_key", KEY);
                params.put("q", candidate.getName());
                params.put("party", candidate.getParty().substring(0,3));
                params.put("office", getFecOfficeString(candidate.getOffice()));
                params.put("state", state);
                params.put("is_active_candidate", true);
                params.put("cycle", year);

                client.get(URL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess candidatesMoney");
                        JSONObject jsonObject = json.jsonObject;
                        try{
                            parseCandidateMoney(jsonObject.getJSONArray("results"), candidate);
                        }
                        catch (JSONException e){
                            Log.e(TAG, "Hit json exception while parcing, error: " + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, String.format("onFailure CandidatesMoney: \nstatusCode:%d \nresponse:%s", statusCode, response));
                    }
                });
            }
        }

        //fills out incumbent status and slogan of candidate
        private static void parseCandidateMoney(JSONArray jsonArray, Candidate candidate) throws JSONException {
            if (jsonArray.length() == 0){
                Log.e(TAG, "0 result for candidate: " + candidate.getName() + ", " + jsonArray);
                return;
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                long cash = jsonObject.getLong("cash_on_hand_end_period");
                long disbursements = jsonObject.getLong("disbursements");
                if (cash + disbursements != 0){
                    candidate.setMoney_raised(cash + disbursements);
                    break;
                }

            }

        }


        //sets candidates slogan and imcumbent status
        public static void setCandidatesBasic(final List<Candidate> candidates, String state) {
            final String URL = BASE_URL + "candidates/search/";
            for (final Candidate candidate : candidates){
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("api_key", KEY);
                params.put("name", candidate.getName());
                params.put("party", candidate.getParty().substring(0,3));
                params.put("office", getFecOfficeString(candidate.getOffice()));
                params.put("state", state);
                params.put("is_active_candidate", true);

                client.get(URL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.d(TAG, "onSuccess candidatesBasic");
                        JSONObject jsonObject = json.jsonObject;
                        try{
                            parseCandidateBasic(jsonObject.getJSONArray("results"), candidate);
                        }
                        catch (JSONException e){
                            Log.e(TAG, "Hit json exception while parcing, error: " + e);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.d(TAG, String.format("onFailure CandidatesBasic: \nstatusCode:%d \nresponse:%s", statusCode, response));
                    }
                });
            }
        }

        //fills out incumbent status and slogan of candidate
        private static void parseCandidateBasic(JSONArray jsonArray, Candidate candidate) throws JSONException {
            if (jsonArray.length() > 1) {
                Log.i(TAG, "more than 1 result for candidate" + candidate.getName() + ", " + jsonArray);
            }
            else if (jsonArray.length() == 0){
                Log.e(TAG, "0 result for candidate: " + candidate.getName() + ", " + jsonArray);
                return;
            }

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            candidate.setIncumbentStatus(jsonObject.getString("incumbent_challenge_full"));
            String slogan = jsonObject.getJSONArray("principal_committees").getJSONObject(0).getString("name");
            candidate.setSlogan(GoogleAPI.capitalizeWord(slogan.toLowerCase()));
        }

    }

    public static class ElectionParse{

        /**
         * queries all president, senate, and house elections in user's state
         * @param user the current user
         */
        public static void getElections(final User user) {
            final String URL = BASE_URL + "election-dates/";
            AsyncHttpClient client = new AsyncHttpClient();

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String date = formatter.format(new Date());

            String state = user.getState();

            String complete = URL + "?api_key=" + KEY + String.format("&min_election_date=%s&office_sought=H&office_sought=S&office_sought=P&sort=-election_date&election_state=%s&page=1", date, state);

            client.get(complete, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess FEC elections");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        parseElection(jsonObject.getJSONArray("results"), user);
                        Log.i(TAG, user.getElectionsList().toString());
                        FecAPI.CandidateParse.getCandidates(user);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure FEC elections: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }

        /**
         * Cycles through array of elections and turns them into an Election and adds to user.elections if not already in list
         * @param jsonArray array of elections
         * @param user current user
         * @throws JSONException if object not in jsonObject
         */
        private static void parseElection(JSONArray jsonArray, User user) throws JSONException {
            List<Election> userElections = user.getElectionsList();
            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Election election = new Election();

                String electionType = jsonObject.getString("election_type_full");
                String state = getFullState(user.getState());
                Offices office = parseOffice(jsonObject.getString("office_sought"));

                election.setOffice(office);
                election.setName(state + " " + electionType);
                election.setDate(parseElectionTime(jsonObject.getString("election_date")));
                election.setCandidates(new ArrayList<Candidate>());

                if (!userElections.contains(election)){
                    userElections.add(election);
                }

            }

        }

        /**
         * converts a string with a date to a date object
         * @param rawJsonTime date in the form of yyyy-mm-dd
         * @return Date object
         */
        public static Date parseElectionTime(String rawJsonTime){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try{
                date = dateFormat.parse(rawJsonTime);
            } catch (ParseException e) {
                Log.e(TAG, "error parsing date");
                e.printStackTrace();
            }
            return date;
        }

        /**
         * @param state abbreviated state name
         * @return full state name including if it is a state (ex. MI -> Michigan State)
         */
        private static String getFullState(String state){
            Map<String, String> states = new HashMap<String, String>();
            states = new HashMap<String, String>();
            states.put("AL", "Alabama State");
            states.put("AK", "Alaska State");
            states.put("AB", "Alberta State");
            states.put("AZ", "Arizona State");
            states.put("AR", "Arkansas State");
            states.put("BC", "British Columbia");
            states.put("CA", "California State");
            states.put("CO", "Colorado State");
            states.put("CT", "Connecticut State");
            states.put("DE", "Delaware State");
            states.put("DC", "District Of Columbia");
            states.put("FL", "Florida State");
            states.put("GA", "Georgia State");
            states.put("GU", "Guam State");
            states.put("HI", "Hawaii State");
            states.put("ID", "Idaho State");
            states.put("IL", "Illinois State");
            states.put("IN", "Indiana State");
            states.put("IA", "Iowa State");
            states.put("KS", "Kansas State");
            states.put("KY", "Kentucky State");
            states.put("LA", "Louisiana State");
            states.put("ME", "Maine State");
            states.put("MB", "Manitoba");
            states.put("MD", "Maryland State");
            states.put("MA", "Massachusetts State");
            states.put("MI", "Michigan State");
            states.put("MN", "Minnesota State");
            states.put("MS", "Mississippi State");
            states.put("MO", "Missouri State");
            states.put("MT", "Montana State");
            states.put("NE", "Nebraska State");
            states.put("NV", "Nevada State");
            states.put("NB", "New Brunswick");
            states.put("NH", "New Hampshire State");
            states.put("NJ", "New Jersey State");
            states.put("NM", "New Mexico State");
            states.put("NY", "New York State");
            states.put("NF", "Newfoundland");
            states.put("NC", "North Carolina State");
            states.put("ND", "North Dakota State");
            states.put("NT", "Northwest Territories");
            states.put("NS", "Nova Scotia");
            states.put("NU", "Nunavut");
            states.put("OH", "Ohio State");
            states.put("OK", "Oklahoma State");
            states.put("ON", "Ontario");
            states.put("OR", "Oregon State");
            states.put("PA", "Pennsylvania State");
            states.put("PE", "Prince Edward Island");
            states.put("PR", "Puerto Rico");
            states.put("QC", "Quebec");
            states.put("RI", "Rhode Island State");
            states.put("SK", "Saskatchewan");
            states.put("SC", "South Carolina State");
            states.put("SD", "South Dakota State");
            states.put("TN", "Tennessee State");
            states.put("TX", "Texas State");
            states.put("UT", "Utah State");
            states.put("VT", "Vermont State");
            states.put("VI", "Virgin Islands");
            states.put("VA", "Virginia State");
            states.put("WA", "Washington State");
            states.put("WV", "West Virginia State");
            states.put("WI", "Wisconsin State");
            states.put("WY", "Wyoming State");
            states.put("YT", "Yukon Territory");
            return states.get(state);
        }

    }

    public static class CandidateParse{

        /**
         * queries all president, senate, and house elections in user's state
         * @param user the current user
         */
        public static void getCandidates(final User user) {
            final String URL = BASE_URL + "candidates/";
            AsyncHttpClient client = new AsyncHttpClient();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int year = cal.get(Calendar.YEAR);
            if (year % 2 != 0){
                cal.add(Calendar.YEAR, 1);
            }
            String date = formatter.format(cal.getTime());

            String state = user.getState();
            String district = user.getDistrict();

            final String complete = URL + "?api_key=" + KEY + String.format("&cycle=%s&state=%s&candidate_status=C&is_active_candidate=true&district=%s&district=00&office=H&office=S&office=P", date, state, district);

            client.get(complete, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess FEC get candidates");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        parseCandidates(jsonObject.getJSONArray("results"), user);
                        FecAPI.CandidateInfoParse.setCandidatesInfo(user);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure FEC get candidates: \nstatusCode:%d \nresponse:%s \n url: %s", statusCode, response, complete));
                }
            });
        }

        /**
         * Cycles through array of candidates and turns them into a Candidate and adds it to the correct election
         * @param jsonArray array of candidates
         * @param user current user
         * @throws JSONException if object not in jsonObject
         */
        private static void parseCandidates(JSONArray jsonArray, User user) throws JSONException {
            List<Election> elections = user.getElectionsList();
            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Candidate candidate = new Candidate();

                candidate.setName(parseName(jsonObject.getString("name")));
                candidate.setParty(parseParty(jsonObject.getString("party_full")));
                candidate.setOffice(parseOffice(jsonObject.getString("office_full")));

                for (Election election: elections){
                    if (election.getOffice() == candidate.getOffice()){
                        if (!election.getCandidates().contains(candidate)){
                            election.getCandidates().add(candidate);
                            GoogleAPI.ImageParse.setImage(candidate);
                        }
                    }
                }

            }
        }

        /**
         * returns Office type for office
         * @param office type of office
         * @return Office type of string
         */
        private static Offices parseOffice(String office){
            if (office.equals("Senate")){
                return Offices.SENATE;
            }
            if (office.equals("House")){
                return Offices.HOUSE_OF_REPRESENTATIVES;
            }
            else{
                return Offices.PRESIDENT;
            }
        }

        /**
         * returns just party name correctly capitilized
         * @param party full party name (ex. REPUBLICAN PARTY)
         * @return simple party name (ex. Republican)
         */
        private static String parseParty(String party){
            int index = party.indexOf(" ");
            if (index != -1){
                return capitalizeWord(party.substring(0, index));
            }
            return capitalizeWord(party);
        }

        /**
         * takes a name in last name, first name format and returns name in first name last name format
         * @param name name in format of lastName, firstName (middle name) suffix
         * @return name as firstName lastName correctly capitilized
         */
        private static String parseName(String name){
            int indexComma = name.indexOf(",");
            String first = name.substring(0, indexComma);
            int indexSpace = name.indexOf(" ", indexComma + 2);
            if (indexSpace < 0){
                indexSpace = name.length();
            }
            name = name.substring(indexComma + 2, indexSpace) + " " + name.substring(0, indexComma);
            return capitalizeWord(name);
        }

        /**
         * takes a string and capitalizes the beginning of every word
         * @param str string of words
         * @return string of words with correct capitalization
         */
        private static String capitalizeWord(String str){
            str = str.toLowerCase();
            String words[] = str.split("\\s");
            String capitalizeWord = "";
            for(String word : words){
                String first = word.substring(0,1);
                String rest = word.substring(1);
                capitalizeWord += first.toUpperCase() + rest + " ";
            }
            return capitalizeWord.trim();
        }


    }

}