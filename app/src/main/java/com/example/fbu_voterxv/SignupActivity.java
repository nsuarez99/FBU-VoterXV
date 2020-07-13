package com.example.fbu_voterxv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbu_voterxv.fragments.ProfileFragment;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "SignupActivity";
    EditText signupUsername;
    EditText signupPassword;
    EditText signupEmail;
    Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupUsername = findViewById(R.id.signupUsername);
        signupPassword = findViewById(R.id.signupPassword);
        signupButton = findViewById(R.id.loginButton);
        signupEmail = findViewById(R.id.signupEmail);
    }

    public void signupClick(View view) {
        //TODO update this to send to correct activity and user
        String username = signupUsername.getText().toString();
        String password = signupPassword.getText().toString();
        String email = signupEmail.getText().toString();

        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "Created Account", Toast.LENGTH_SHORT);
                    Log.i(TAG, "Succesfully created user");
                    Intent i = new Intent(SignupActivity.this, MainActivity.class);
                    i.putExtra("signup", true);
                    startActivity(i);
                    finish();

                } else {
                    Log.e(TAG, "Error while creating user", e);
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}