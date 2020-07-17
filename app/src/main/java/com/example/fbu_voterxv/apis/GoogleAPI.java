package com.example.fbu_voterxv.apis;

import android.graphics.Movie;
import android.text.format.DateUtils;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_voterxv.BuildConfig;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Headers;

public class GoogleAPI {

    public static final String TAG = "GoogleAPI";
    private static final String BASE_URL = "https://www.googleapis.com/civicinfo/v2/";
    private static final String KEY = BuildConfig.GOOGLE_KEY;
    //TODO check president tag

    //sets myOfficials and district
    public static void setMyOfficials(final User user) {
        final String URL = BASE_URL + "representatives";
        AsyncHttpClient client = new AsyncHttpClient();

        //TODO figure out if can split this into parameter without roles overrideing
        String complete = URL + "?address=" + user.getAddress() + "&levels=country&roles=legislatorLowerBody&roles=legislatorUpperBody&roles=headOfGovernment&roles=deputyHeadOfGovernment&key=" + KEY;

        client.get(complete, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess setMyOfficials");
                JSONObject jsonObject = json.jsonObject;
                try{
                    user.setDistrict(jsonObject);
                    user.setOfficials(MyOfficials.fromJsonObject(jsonObject));
                    Log.i(TAG, user.getOfficials().toString());
                }
                catch (JSONException e){
                    Log.e(TAG, "Hit json exception while parcing, error: " + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, String.format("onFailure Officials: \nstatusCode:%d \nresponse:%s", statusCode, response));
            }
        });
    }



    public static class ElectionParse{

        private static final String ELECTION_CONGRESSMAN = "Representative In Congress";
        private static final String ELECTION_SENATOR = "United States Senator";
        private static final String ELECTION_PRESIDENT = "United States President";

        //sets myOfficials and district
        public static void setElections(final User user) {
            final String URL = BASE_URL + "voterinfo";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("key", KEY);
            params.put("address", user.getAddress());
            params.put("officialOnly", false);

            client.get(URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess setElection");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        user.setElectionsList(parseElectionList(jsonObject));
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure Elections: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }


        //creates a list of Elections
        public static List<Election> parseElectionList(JSONObject jsonObject) throws JSONException {
            String election_name = jsonObject.getJSONObject("election").getString("name");
            Date election_day = parseElectionTime(jsonObject.getJSONObject("election").getString("electionDay"));
            List<Election> electionsList = new ArrayList<>();

            JSONArray electionsJsonArray = jsonObject.getJSONArray("contests");

            for (int i = 0; i < electionsJsonArray.length(); i++) {
                JSONObject electionsJSONObject = electionsJsonArray.getJSONObject(i);

                Election election = new Election();
                election.setDate(election_day);

                String office = electionsJSONObject.getString("office");
                if (office.equals(ELECTION_CONGRESSMAN.toUpperCase())){
                    election.setName(election_name + " - " + ELECTION_CONGRESSMAN);
                    parseCandidate(electionsJSONObject, election);
                    electionsList.add(election);
                }
                else if (office.equals(ELECTION_SENATOR.toUpperCase())){
                    election.setName(election_name + " - " + ELECTION_SENATOR);
                    parseCandidate(electionsJSONObject, election);
                    electionsList.add(election);
                }
                else if (office.equals(ELECTION_PRESIDENT.toUpperCase())){
                    election.setName(election_name + " - " + ELECTION_PRESIDENT);
                    parseCandidate(electionsJSONObject, election);
                    electionsList.add(election);
                }
            }
            return electionsList;
        }

        //creates a single Election
        private static void parseCandidate(JSONObject jsonObject, Election election) throws JSONException {
            JSONArray candidatesJsonArray = jsonObject.getJSONArray("candidates");
            List<Candidate> candidates = new ArrayList<>();
            election.setCandidates(candidates);

            //set candidate name and party and add to list
            for (int i = 0; i < candidatesJsonArray.length() ; i++) {
                JSONObject candidatesJsonObject = candidatesJsonArray.getJSONObject(i);
                Candidate candidate = new Candidate();
                candidate.setName(candidatesJsonObject.getString("name"));
                candidate.setParty(capitalizeWord(candidatesJsonObject.getString("party")));
                candidates.add(candidate);
            }
        }

        public static String capitalizeWord(String str){
            String words[] = str.split("\\s");
            String capitalizeWord = "";
            for(String word : words){
                String first = word.substring(0,1);
                String rest = word.substring(1);
                capitalizeWord += first.toUpperCase() + rest + " ";
            }
            return capitalizeWord.trim();
        }

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
    }

}