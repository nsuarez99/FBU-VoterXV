package com.example.fbu_voterxv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fbu_voterxv.apis.FecAPI;
import com.example.fbu_voterxv.apis.GoogleAPI;
import com.example.fbu_voterxv.fragments.ElectionsFragment;
import com.example.fbu_voterxv.fragments.OfficialsFragment;
import com.example.fbu_voterxv.fragments.ProfileFragment;
import com.example.fbu_voterxv.fragments.SettingsFragment;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivityTag";
    private Toolbar toolbar;
    private ImageView profileImage;
    private BottomNavigationView bottomNavigationView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        // define your fragments here
        final Fragment officialsFragment = new OfficialsFragment();
        final Fragment electionsFragment = new ElectionsFragment();
        final Fragment settingsFragment = new SettingsFragment();
        final Fragment profileFragment = new ProfileFragment();

        profileImage = findViewById(R.id.profile);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setSupportActionBar(toolbar);

        //set toolbar composing and setting action
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment;
                if (item.getItemId() == R.id.settings) {
                    fragment = settingsFragment;
                    fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
                }
                return true;
            }
        });

        //Create my user object and populate with parseUser data
        user = new User();
        populateUser();

        //checks to see if it comes from signup page to send them to the profile page
        if(getIntent().getBooleanExtra("signup", false)){
            Log.i(TAG, "new user");
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", Parcels.wrap(user));
            Fragment fragment = profileFragment;
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, profileFragment).commit();
        }

        //set up bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.officialsIcon:
                        fragment = officialsFragment;
                        break;
                    case R.id.electionsIcon:
                        fragment = electionsFragment;
                        break;
                    default:
                        fragment = officialsFragment;
                        break;
                }

                //pass user data with fragment
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", Parcels.wrap(user));
                fragment.setArguments(bundle);

                //set fragment
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.officialsIcon);


        //set up profile image click to profile fragment
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", Parcels.wrap(user));
                Fragment fragment = profileFragment;
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
            }
        });

    }

    //creates new user from parseUser data
    public void populateUser() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        user.setEmail(parseUser.getEmail());
        user.setStreet(parseUser.getString("street"));
        user.setCity(parseUser.getString("city"));
        user.setState(parseUser.getString("state"));
        user.setZipcode(parseUser.getString("zipcode"));
        user.setParty(parseUser.getString("party"));
        user.setAge(parseUser.getInt("age"));
        user.setImage(parseUser.getParseFile("image"));

        GoogleAPI.OfficialsParse.setMyOfficials(user);
        GoogleAPI.ElectionParse.setElections(user);


        FecAPI.CandidateParse.setCandidates(user.getElectionsList());

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}