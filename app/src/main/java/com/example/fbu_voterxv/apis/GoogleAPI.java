package com.example.fbu_voterxv.apis;

import android.graphics.Movie;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_voterxv.BuildConfig;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class GoogleAPI {

    public static final String TAG = "GoogleAPI";
    private static final String BASE_URL = "https://www.googleapis.com/civicinfo/v2/";
    private static final String KEY = BuildConfig.GOOGLE_KEY;

    //sets myOfficials and district
    public static void setMyOfficials(final User user) {
        final String URL = BASE_URL + "representatives";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("key", KEY);
        params.put("address", user.getAddress());
        params.put("levels", "country");
        params.put("roles", "legislatorLowerBody");
        client.get(URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess setMyOfficials");
                JSONObject jsonObject = json.jsonObject;
                try{
                    user.setDistrict(jsonObject);
//                    user.setOfficials(MyOfficials.fromJsonObject(jsonObject));
                    Log.i(TAG, user.getDistrict());
                }
                catch (JSONException e){
                    Log.e(TAG, "Hit json exception while parcing, error: " + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, String.format("onFailure: \nstatusCode:%d \nresponse:%s", statusCode, response));
            }
        });
    }

}
