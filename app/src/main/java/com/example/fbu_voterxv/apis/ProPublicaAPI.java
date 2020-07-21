package com.example.fbu_voterxv.apis;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_voterxv.BuildConfig;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Headers;

public class ProPublicaAPI {

    public static final String TAG = "ProPublicaAPI";
    private static final String BASE_URL = "https://api.propublica.org/congress/v1/members/";
    private static final String KEY = BuildConfig.PROPPUBLICA_KEY;


    public static class OfficialsParse{

        //sets senators, congressman years, committees, party
        public static void setRepBasicInfo(final User user) {
            setCongressmanInfo(user);
            setSenatorsInfo(user);
        }

        private static void setSenatorsInfo(final User user){
            String chamber = "senate";
            String state = user.getState();
            String arguments = String.format("/%s/%s/current.json", chamber, state);
            final String URL = BASE_URL + arguments;

            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess senateYears");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseReps(jsonArray, user.getOfficials().getJuniorSenator());
                        parseReps(jsonArray, user.getOfficials().getSeniorSenator());
                        setJuniorSeniorSenator(user.getOfficials());
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure senatorYears: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }

        private static void setCongressmanInfo(final User user){
            String chamber = "house";
            String state = user.getState();
//            String district = user.getDistrict();
            String arguments = String.format("/%s/%s/current.json", chamber, state);
            final String URL = BASE_URL + arguments;

            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess congressmanYears");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseReps(jsonArray, user.getOfficials().getCongressman());
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure congressmanYears: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }

        //setting the correct order of junior and senior senators
        private static void setJuniorSeniorSenator(MyOfficials myOfficials){
            Representative seniorSenator = myOfficials.getSeniorSenator();
            Representative juniorSenator = myOfficials.getJuniorSenator();
            int juniorYears = Integer.parseInt(juniorSenator.getYears().substring(juniorSenator.getYears().length() - 4));
            int seniorYears = Integer.parseInt(seniorSenator.getYears().substring(seniorSenator.getYears().length() - 4));

            if (juniorYears < seniorYears){
                myOfficials.setJuniorSenator(seniorSenator);
                myOfficials.setSeniorSenator(juniorSenator);
            }

        }

        //parse fromJson Object party, committee, years
        private static void parseReps(JSONArray jsonArray, Representative representative) throws JSONException {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (nameEquals(representative.getName(), jsonObject.getString("name"))){

                    //set years in office
                    int years = Integer.parseInt(jsonObject.getString("seniority"));
                    String electionYear = jsonObject.getString("next_election");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy");
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR, years * -1);
                    String startYear = format.format(calendar.getTime());
                    representative.setYears(startYear + " - " + electionYear);

                    //set party
                    String party = jsonObject.getString("party");
                    representative.setParty(parseParty(party));

                    //set committees
                    String id = jsonObject.getString("id");
                    getCommittees(id, representative);
                    return;
                }
            }
        }

        private static String parseParty(String party){
            if (party.equals("I")){
                return "Independent";
            }
            else if (party.equals("D")){
                return "Democrat";
            }
            if (party.equals("R")){
                return "Republican";
            }
            if (party.equals("L")){
                return "Libertarian";
            }
            return party;
        }

        private static boolean nameEquals(String repName, String jsonObjectName){
            String[] repNameArray = repName.split("\\s+");
            String[] jsonObjectNameArray = jsonObjectName.split("\\s+");

            //just in case someone has a double last name
            if (repNameArray.length >= 3){
                repNameArray[1] = "";
            }
            if (jsonObjectNameArray.length >= 3){
                jsonObjectNameArray[1] = "";
            }
            repName = arrayToString(repNameArray);
            jsonObjectName = arrayToString(jsonObjectNameArray);
            return repName.equals(jsonObjectName);
        }

        private static String arrayToString(String[] array){
            String word = "";
            for (int i = 0; i < array.length ; i++) {
                word += array[i];
            }
            return word;
        }

        private static void getCommittees(String id, final Representative representative){
            final String URL = BASE_URL + id + ".json";

            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess repCommittee: " + representative.getName());
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseCommittee(jsonArray, representative);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure repCommittee: %s \nstatusCode:%d \nresponse:%s", representative.getName(), statusCode, response));
                }
            });
        }

        private static void parseCommittee(JSONArray jsonArray, Representative representative) throws JSONException {
            if (jsonArray.length() > 1) {
                Log.e(TAG, "more than 1 result for representative: "  + jsonArray);
                return;
            }
            else if (jsonArray.length() == 0){
                Log.e(TAG, "0 result for representative: " + jsonArray);
                return;
            }

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject latestCongress = jsonObject.getJSONArray("roles").getJSONObject(0);
            JSONArray committees = latestCongress.getJSONArray("committees");
            String committeeString = "";
                for (int i = 0; i < committees.length() ; i++) {
                    JSONObject committee = committees.getJSONObject(i);
                    committeeString += committee.getString("name") + ", ";
                }
            if (committees.length() != 0){
                committeeString = committeeString.substring(0, committeeString.length() - 2);
                representative.setCommittee(committeeString);
            }
            else{
                representative.setCommittee("None");
            }
        }
    }


}