package com.example.fbu_voterxv.apis;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
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

public class BingAPI {

    public static final String TAG = "BingAPI";
    private static final String BASE_URL = "https://api.cognitive.microsoft.com/bing/v7.0/";
    private static final String KEY = BuildConfig.BING_KEY;

    public static class ImageParse{

        public static void setImage(final Politician politician){
            final String URL = BASE_URL + "images/search";
            AsyncHttpClient client = new AsyncHttpClient();

            RequestHeaders headers = new RequestHeaders();
            headers.put("Ocp-Apim-Subscription-Key", KEY);

            RequestParams params = new RequestParams();
            params.put("q", politician.getName() + " congress");

            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess Bing setImage");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        parseImage(jsonObject.getJSONArray("value"), politician);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure Bing setImage: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }


        private static void parseImage(JSONArray jsonArray, Politician politician) throws JSONException {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            politician.setProfileImage(jsonObject.getString("thumbnailUrl"));
        }
    }
}