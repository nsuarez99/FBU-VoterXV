package com.example.fbu_voterxv.apis;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_voterxv.BuildConfig;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.fragments.OfficialsFragment;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Politician;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

public class GoogleAPI {

    public static final String TAG = "GoogleAPI";
    private static final String BASE_URL = "https://www.googleapis.com/civicinfo/v2/";
    private static final String KEY = BuildConfig.GOOGLE_KEY;

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

    public static class OfficialsParse{

        //sets myOfficials and district
        public static void setMyOfficials(final User user, final FragmentTransaction fragmentTransaction, final Map<String, Set<Bill>> bills) {
            final String URL = BASE_URL + "representatives";
            AsyncHttpClient client = new AsyncHttpClient();

            String complete = URL + "?address=" + user.getAddress() + "&levels=country&roles=legislatorLowerBody&roles=legislatorUpperBody&roles=headOfGovernment&roles=deputyHeadOfGovernment&key=" + KEY;

            client.get(complete, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess setMyOfficials");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        parseDistrict(jsonObject, user);
                        user.setOfficials(parseMyOfficials(jsonObject, user));
                        Log.i(TAG, user.getOfficials().toString());
                        ProPublicaAPI.OfficialsBasicParse.setRepBasicInfo(user);

                        //reload myofficials page
                        Fragment fragment = new OfficialsFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("user", Parcels.wrap(user));
                        bundle.putParcelable("bills", Parcels.wrap(bills));
                        fragment.setArguments(bundle);
                        fragmentTransaction.replace(R.id.layoutContainer, fragment).commit();
                        Log.i(TAG, "reloaded officials");
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

        //parse MyOfficials fromJson Object
        private static MyOfficials parseMyOfficials(JSONObject jsonObject, User user) throws JSONException {
            MyOfficials myOfficials = new MyOfficials();
            JSONArray officesArray = jsonObject.getJSONArray("officials");
            for (int i = 0; i < officesArray.length() ; i++) {
                JSONObject official = officesArray.getJSONObject(i);

                Representative representative = new Representative();

                //set name
                representative.setName(official.getString("name"));

                //set party
                String party = official.getString("party");
                int space_index = party.indexOf(" ");
                if (space_index == -1){
                    space_index = party.length();
                }
                representative.setParty(party.substring(0, space_index));

                //set photoURL if available
                if (official.has("photoUrl")){
                    representative.setProfileImage(official.getString("photoUrl"));
                }
                else{
                    GoogleAPI.ImageParse.setImage(representative);
                }

                //set website
                representative.setWebsite(official.getJSONArray("urls").getString(0));

                //set social media accounts if available
                JSONArray channels = official.getJSONArray("channels");
                representative.setFb("N/A");
                representative.setTwitter("N/A");
                for (int j = 0; j < channels.length() ; j++) {
                    JSONObject channel = channels.getJSONObject(j);
                    if (channel.getString("type").equals("Facebook")){
                        representative.setFb(channel.getString("id"));
                    }
                    else if (channel.getString("type").equals("Twitter")){
                        representative.setTwitter(channel.getString("id"));
                    }
                }

                //TODO find API so don't have to hard code president dates
                //set representative as official
                if (i == 0){
                    representative.setOffice(Offices.PRESIDENT);
                    representative.setYears("2016 - 2020");
                    myOfficials.setPresident(representative);
                }
                else if (i == 1){
                    representative.setOffice(Offices.VICE_PRESIDENT);
                    representative.setYears("2016 - 2020");
                    myOfficials.setVicePresident(representative);
                }
                else if (i == 2){
                    representative.setOffice(Offices.SENATE);
                    representative.setState(user.getState());
                    myOfficials.setSeniorSenator(representative);
                }
                else if (i == 3){
                    representative.setOffice(Offices.SENATE);
                    representative.setState(user.getState());
                    myOfficials.setJuniorSenator(representative);
                }
                else if (i == 4){
                    representative.setOffice(Offices.HOUSE_OF_REPRESENTATIVES);
                    representative.setState(user.getState());
                    myOfficials.setCongressman(representative);
                }
            }
            return myOfficials;
        }

        private static void parseDistrict(JSONObject jsonObject, User user) throws JSONException {
            JSONArray officesArray = jsonObject.getJSONArray("offices");
            for (int i = 0; i < officesArray.length(); i++){
                JSONObject office = officesArray.getJSONObject(i);
                if (office.getString("name").equals("U.S. Representative")){
                    int index = office.getString("divisionId").indexOf("cd:");
                    user.setDistrict(office.getString("divisionId").substring(index + 3));
                    return;
                }
            }
        }
    }

    public static class ElectionParse{

        //TODO checkmark president tag
        private static final String ELECTION_CONGRESSMAN = "REPRESENTATIVE IN CONGRESS";
        private static final String ELECTION_SENATOR = "UNITED STATES SENATOR";
        private static final String ELECTION_PRESIDENT = "UNTIED STATES PRESIDENT";

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
                        FecAPI.ElectionParse.getElections(user);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    user.setElectionsList(new ArrayList<Election>());
                    FecAPI.ElectionParse.getElections(user);
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
                if (office.equals(ELECTION_CONGRESSMAN)){
                    election.setOffice(Offices.HOUSE_OF_REPRESENTATIVES);
                    election.setName(election_name);
                    parseCandidate(electionsJSONObject, election, Offices.HOUSE_OF_REPRESENTATIVES);
                    electionsList.add(election);
                }
                else if (office.equals(ELECTION_SENATOR)){
                    election.setOffice(Offices.SENATE);
                    election.setName(election_name);
                    parseCandidate(electionsJSONObject, election, Offices.SENATE);
                    electionsList.add(election);
                }
                else if (office.equals(ELECTION_PRESIDENT)){
                    election.setOffice(Offices.PRESIDENT);
                    election.setName(election_name);
                    parseCandidate(electionsJSONObject, election, Offices.PRESIDENT);
                    electionsList.add(election);
                }
            }
            return electionsList;
        }

        //creates a single Election
        private static void parseCandidate(JSONObject jsonObject, Election election, Offices office) throws JSONException {
            JSONArray candidatesJsonArray = jsonObject.getJSONArray("candidates");
            List<Candidate> candidates = new ArrayList<>();
            election.setCandidates(candidates);

            //set candidate name and party and add to list
            for (int i = 0; i < candidatesJsonArray.length() ; i++) {
                JSONObject candidatesJsonObject = candidatesJsonArray.getJSONObject(i);
                Candidate candidate = new Candidate();
                candidate.setName(candidatesJsonObject.getString("name"));
                candidate.setParty(capitalizeWord(candidatesJsonObject.getString("party").toLowerCase()));
                GoogleAPI.ImageParse.setImage(candidate);
                candidate.setOffice(office);
                candidates.add(candidate);
            }
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

    public static class ImageParse{

        public static void setImage(final Politician politician){
            final String URL = "https://www.googleapis.com/customsearch/v1";
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("key", KEY);
            params.put("q", politician.getName() + " congress " + politician.getParty());
            params.put("searchType", "image");
            params.put("num", 1);
            params.put("cx", "014531769706675276559:ohhclcewsxs");

            client.get(URL, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess setImage");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        parseImage(jsonObject.getJSONArray("items"), politician);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure setImage: \nstatusCode:%d \nresponse:%s", statusCode, response));
                    BingAPI.ImageParse.setImage(politician);
                }
            });
        }


        private static void parseImage(JSONArray jsonArray, Politician politician) throws JSONException {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            politician.setProfileImage(jsonObject.getString("link"));
        }
    }
}