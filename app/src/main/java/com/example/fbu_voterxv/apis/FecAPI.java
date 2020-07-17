package com.example.fbu_voterxv.apis;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_voterxv.BuildConfig;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static class CandidateParse{

        public static void setCandidates(final List<Election> elections){
            for (Election election : elections){
                List<Candidate> candidates = election.getCandidates();
                setCandidatesBasic(candidates);
                setCandidatesMoney(candidates);
            }
        }

        //sets candidates money raised
        public static void setCandidatesMoney(final List<Candidate> candidates) {
            final String URL = BASE_URL + "candidates/totals/";
            for (final Candidate candidate : candidates){
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("api_key", KEY);
                params.put("name", candidate.getName());
                params.put("party", candidate.getParty().substring(3));
                params.put("office", getFecOfficeString(candidate.getOffice()));
                params.put("is_active_candidate", true);

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
            if (jsonArray.length() > 1) {Log.e(TAG, "more than 1 result for candidate");}

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            long cash = jsonObject.getLong("cash_on_hand_end_period");
            long disbursements = jsonObject.getLong("disbursements");
            candidate.setMoney_raised(cash + disbursements);
        }


        //sets candidates slogan and imbumbent status
        public static void setCandidatesBasic(final List<Candidate> candidates) {
            final String URL = BASE_URL + "candidates/search/";
            for (final Candidate candidate : candidates){
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("api_key", KEY);
                params.put("name", candidate.getName());
                params.put("party", candidate.getParty().substring(3));
                params.put("office", getFecOfficeString(candidate.getOffice()));
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
            if (jsonArray.length() > 1) {Log.e(TAG, "more than 1 result for candidate");}

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            candidate.setIncumbentStatus(jsonObject.getString("incumbent_challenge_full"));
            candidate.setSlogan(jsonObject.getJSONArray("principal_committees").getJSONObject(0).getString("name"));
        }

    }

}