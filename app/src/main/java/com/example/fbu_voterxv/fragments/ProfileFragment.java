package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbu_voterxv.MainActivity;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;


public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private EditText profileAge;
    private EditText profileParty;
    private EditText profileStreet;
    private EditText profileCity;
    private EditText profileState;
    private EditText profileZipcode;
    private User user;
    private Button saveButton;

    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = Parcels.unwrap(getArguments().getParcelable("user"));

        profileAge = getActivity().findViewById(R.id.profileAge);
        profileParty = getActivity().findViewById(R.id.profileParty);
        profileStreet = getActivity().findViewById(R.id.profileStreet);
        profileCity = getActivity().findViewById(R.id.profileCity);
        profileState = getActivity().findViewById(R.id.profileState);
        profileZipcode = getActivity().findViewById(R.id.profileZipcode);
        saveButton = getActivity().findViewById(R.id.saveButton);

        populateFields();

        //update profile when button is clicked
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileStreet.getText().toString().isEmpty() || profileCity.getText().toString().isEmpty() || profileState.getText().toString().isEmpty() || profileZipcode.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Must fill in address", Toast.LENGTH_SHORT).show();
                }
                else{
                    saveChanges();
                }
            }
        });

    }

    //save changes
    public void saveChanges(){
        ParseUser parseUser = ParseUser.getCurrentUser();

        int age = Integer.parseInt(profileAge.getText().toString());
        String party = profileParty.getText().toString();
        String street = profileStreet.getText().toString();
        String city = profileCity.getText().toString();
        String state = profileState.getText().toString();
        String zipcode = profileZipcode.getText().toString();

        //update parseUser
        parseUser.put("age", age);
        parseUser.put("party", party);
        parseUser.put("street", street);
        parseUser.put("city", city);
        parseUser.put("state", state);
        parseUser.put("zipcode", zipcode);
        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                ((MainActivity) getActivity()).populateUser();
                Toast.makeText(getContext(), "Saved profile", Toast.LENGTH_SHORT).show();
                populateFields();
            }
        });

        //update user
//        user.setAge(age);
//        user.setParty(party);
//        user.setStreet(street);
//        user.setCity(city);
//        user.setState(state);
//        user.setZipcode(zipcode);


    }

    //populate fields
    public void populateFields(){
        profileAge.setText(Integer.toString(user.getAge()));
        profileParty.setText(user.getParty());
        profileStreet.setText(user.getStreet());
        profileCity.setText(user.getCity());
        profileState.setText(user.getState());
        profileZipcode.setText(user.getZipcode());
    }
}