package com.example.fbu_voterxv;

import android.app.Application;

import com.example.fbu_voterxv.models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("nsuarez-fbu-voterxv") // should correspond to APP_ID env variable
                .clientKey("fbuVoterXV")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://nsuarez-fbu-voterxv.herokuapp.com/parse/").build());
    }
}