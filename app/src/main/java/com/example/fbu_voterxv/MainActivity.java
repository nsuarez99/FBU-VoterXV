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

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.apis.GoogleAPI;
import com.example.fbu_voterxv.apis.ProPublicaAPI;
import com.example.fbu_voterxv.fragments.ElectionsFragment;
import com.example.fbu_voterxv.fragments.OfficialsFragment;
import com.example.fbu_voterxv.fragments.ProfileFragment;
import com.example.fbu_voterxv.fragments.SettingsFragment;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivityTag";
    private Toolbar toolbar;
    private ImageView profileImage;
    private BottomNavigationView bottomNavigationView;
    private User user;
    private Map<String, Set<Bill>> bills;
    private final Fragment officialsFragment = new OfficialsFragment();
    private final Fragment electionsFragment = new ElectionsFragment();
    private final Fragment settingsFragment = new SettingsFragment();
    private final Fragment profileFragment = new ProfileFragment();
    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileImage = findViewById(R.id.profileToolbar);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setSupportActionBar(toolbar);
        bills = new HashMap<>();
        user = new User();


        //set toolbar composing and setting action
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment fragment;
                if (item.getItemId() == R.id.settings) {
                    fragment = settingsFragment;
                    fragmentManager.beginTransaction().replace(R.id.layoutContainer, fragment).commit();
                }
                return true;
            }
        });


        //Create my user object and populate with parseUser data
        populateUser();

        //get bills data
        ProPublicaAPI.OfficialsVotingParse.getBills(bills);



        //checks to see if it comes from signup page to send them to the profile page
        if(getIntent().getBooleanExtra("signup", false)){
            Log.i(TAG, "new user");
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", Parcels.wrap(user));
            Fragment fragment = profileFragment;
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.layoutContainer, profileFragment).commit();
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
                bundle.putParcelable("bills", Parcels.wrap(bills));
                fragment.setArguments(bundle);

                //set fragment
                fragmentManager.beginTransaction().replace(R.id.layoutContainer, fragment).commit();
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
                fragmentManager.beginTransaction().replace(R.id.layoutContainer, fragment).commit();
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
        if (user.getImage() != null){
            int radius = 100;
            int margin = 0;
            Glide.with(MainActivity.this).load(user.getImage().getUrl()).placeholder(R.drawable.person).transform(new RoundedCornersTransformation(radius, margin)).into(profileImage);
        }

        //get user representatives and election data
        GoogleAPI.OfficialsParse.setMyOfficials(user, fragmentManager.beginTransaction(), bills);
//        GoogleAPI.ElectionParse.setElections(user);

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}