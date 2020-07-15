package com.example.fbu_voterxv.fragments;

import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.User;

import org.parceler.Parcels;


public class OfficialsFragment extends Fragment {

    public static final String TAG = "OfficialsFragment";
    private User user;
    private MyOfficials myOfficials;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private ImageView presidentImage;
    private ImageView vicePresidentImage;
    private ImageView senatorSrImage;
    private ImageView senatorJrImage;
    private ImageView congressmanImage;

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

        //set fragmentManager and fragment
        fragmentManager = getActivity().getSupportFragmentManager();
        fragment = new RepresentativeFragment();

        presidentImage = getActivity().findViewById(R.id.presidentImage);
        vicePresidentImage = getActivity().findViewById(R.id.vicePresidentImage);
        senatorSrImage = getActivity().findViewById(R.id.senatorSrImage);
        senatorJrImage = getActivity().findViewById(R.id.senatorJrImage);
        congressmanImage = getActivity().findViewById(R.id.congressmanImage);

        //set image click listeners
        setListeners();
    }

    //TODO get rid of test represenative once populating data
    //set click listeners to send to fragment with correct data
    private void setListeners() {
        final Representative test = new Representative();

        presidentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(test);
//                goToFragment(myOfficials.getPresident());
            }
        });

        vicePresidentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(test);
//                goToFragment(myOfficials.getVicePresident());
            }
        });

        senatorSrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(test);
//                goToFragment(myOfficials.getSeniorSenator());
            }
        });

        senatorJrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(test);
//                goToFragment(myOfficials.getJuniorSenator());
            }
        });

        congressmanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToFragment(test);
//                goToFragment(myOfficials.getCongressman());
            }
        });
    }

    //set fragment data and send to represenative fragment
    private void goToFragment(Representative representative){
        Bundle bundle = new Bundle();
        bundle.putParcelable("representative", Parcels.wrap(representative));
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
    }
}