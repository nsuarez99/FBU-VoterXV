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

import com.example.fbu_voterxv.fragments.ElectionsFragment;
import com.example.fbu_voterxv.fragments.OfficialsFragment;
import com.example.fbu_voterxv.fragments.ProfileFragment;
import com.example.fbu_voterxv.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private ImageView profileImage;
    private BottomNavigationView bottomNavigationView;

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
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.officialsIcon);


        //set up profile image click to profile fragment
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = profileFragment;
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();

            }
        });

        //checks to see if it comes from signup page to send them to the profile page
        if(getIntent().getBooleanExtra("signup", false)){
            fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, profileFragment).commit();
        }
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
}