package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.Politician;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.User;

import org.parceler.Parcels;

import java.util.List;
import java.util.Map;


public class OfficialsFragment extends Fragment {

    public static final String TAG = "OfficialsFragment";
    private User user;
    private MyOfficials myOfficials;
    private Map<String, List<Bill>> bills;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private ImageView presidentImage;
    private TextView presidentName;
    private TextView presidentParty;
    private ImageView vicePresidentImage;
    private TextView vicePresidentName;
    private TextView vicePresidentParty;
    private ImageView senatorSrImage;
    private TextView senatorSrName;
    private TextView senatorSrParty;
    private ImageView senatorJrImage;
    private TextView senatorJrName;
    private TextView senatorJrParty;
    private ImageView congressmanImage;
    private TextView congressmanName;
    private TextView congressmanParty;

    public OfficialsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_officials, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get officials data
        user = Parcels.unwrap(getArguments().getParcelable("user"));
        myOfficials = user.getOfficials();
        bills = Parcels.unwrap(getArguments().getParcelable("bills"));


        if (myOfficials != null){
            Log.i(TAG, myOfficials.toString());
        }

        //set fragmentManager and fragment
        fragmentManager = getActivity().getSupportFragmentManager();
        fragment = new RepresentativeFragment();

        presidentImage = getActivity().findViewById(R.id.presidentImage);
        presidentName = getActivity().findViewById(R.id.presidentName);
        presidentParty = getActivity().findViewById(R.id.presidentParty);
        vicePresidentImage = getActivity().findViewById(R.id.vicePresidentImage);
        vicePresidentName = getActivity().findViewById(R.id.vicePresidentName);
        vicePresidentParty = getActivity().findViewById(R.id.vicePresidentParty);
        senatorSrImage = getActivity().findViewById(R.id.senatorSrImage);
        senatorSrName = getActivity().findViewById(R.id.senatorSrName);
        senatorSrParty = getActivity().findViewById(R.id.senatorSrParty);
        senatorJrImage = getActivity().findViewById(R.id.senatorJrImage);
        senatorJrName = getActivity().findViewById(R.id.senatorJrName);
        senatorJrParty = getActivity().findViewById(R.id.senatorJrParty);
        congressmanImage = getActivity().findViewById(R.id.congressmanImage);
        congressmanName = getActivity().findViewById(R.id.congressmanName);
        congressmanParty = getActivity().findViewById(R.id.congressmanParty);

        if (myOfficials != null){
            populateOfficials();
        }

        //set image click listeners
        setListeners();
    }

    public void populateOfficials(){
        //set president layout views
        Politician president = myOfficials.getPresident();
        presidentName.setText(president.getName());
        presidentParty.setText(president.getParty());
        Glide.with(getContext()).load(president.getProfileImage()).placeholder(R.drawable.officials).into(presidentImage);

        //set vicepresident layout views
        Politician vicePresident = myOfficials.getVicePresident();
        vicePresidentName.setText(vicePresident.getName());
        vicePresidentParty.setText(vicePresident.getParty());
        Glide.with(getContext()).load(vicePresident.getProfileImage()).placeholder(R.drawable.officials).into(vicePresidentImage);

        //set senior senator layout views
        Politician senatorSr = myOfficials.getSeniorSenator();
        senatorSrName.setText(senatorSr.getName());
        senatorSrParty.setText(senatorSr.getParty());
        Glide.with(getContext()).load(senatorSr.getProfileImage()).placeholder(R.drawable.officials).into(senatorSrImage);

        //set junior senator layout views
        Politician senatorJr = myOfficials.getJuniorSenator();
        senatorJrName.setText(senatorJr.getName());
        senatorJrParty.setText(senatorJr.getParty());
        Glide.with(getContext()).load(senatorJr.getProfileImage()).placeholder(R.drawable.officials).into(senatorJrImage);

        //set congressman layout views
        Politician congressman = myOfficials.getCongressman();
        congressmanName.setText(congressman.getName());
        congressmanParty.setText(congressman.getParty());
        Glide.with(getContext()).load(congressman.getProfileImage()).placeholder(R.drawable.officials).into(congressmanImage);
    }

    //set click listeners to send to fragment with correct data
    private void setListeners() {
        final Representative test = new Representative();

        presidentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(myOfficials.getPresident());
            }
        });

        vicePresidentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(myOfficials.getVicePresident());
            }
        });

        senatorSrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(myOfficials.getSeniorSenator());
            }
        });

        senatorJrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(myOfficials.getJuniorSenator());
            }
        });

        congressmanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(myOfficials.getCongressman());
            }
        });
    }

    //set fragment data and send to represenative fragment
    private void goToFragment(Politician representative){
        Bundle bundle = new Bundle();
        bundle.putParcelable("representative", Parcels.wrap(representative));
        bundle.putParcelable("bills", Parcels.wrap(bills));
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.layoutContainer, fragment).commit();
    }
}