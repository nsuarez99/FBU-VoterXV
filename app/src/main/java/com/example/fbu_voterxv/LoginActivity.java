package com.example.fbu_voterxv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    EditText loginUsername;
    EditText loginPassword;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.signupUsername);
        loginPassword = findViewById(R.id.signupPassword);
        loginButton = findViewById(R.id.loginButton);

        if (ParseUser.getCurrentUser() != null){
            try {
                ParseUser.getCurrentUser().fetch();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            goMainActivity();
        }
    }

    //when login button is clicked attempts to login the user and if valid user goes to MainAcitivity
    public void loginClick(View view){
        Log.i(TAG, "login button clicked, attempting to login");
        String username = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null){
                    //TODO better error handling
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
                else{
                    goMainActivity();
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    //Creates intent to go to SignupAcitivty
    public void signupClick(View view) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }

    //Creates intent to go to MainActivity and finishes this activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}