package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RepresentativeFragment extends Fragment {

    private ImageView image;
    private TextView name;
    private TextView party;
    private TextView committee;
    private TextView years;
    private TextView fb;
    private TextView twitter;
    private TextView office;
    private TextView website;
    private Representative representative;
    private BottomNavigationView bottomNavigationView;


    public RepresentativeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_representative, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        representative = Parcels.unwrap(getArguments().getParcelable("representative"));

        image = getActivity().findViewById(R.id.representativeImage);
        name = getActivity().findViewById(R.id.representativeName);
        party = getActivity().findViewById(R.id.representativeParty);
        committee = getActivity().findViewById(R.id.representativeCommittee);
        years = getActivity().findViewById(R.id.representativeYears);
        fb = getActivity().findViewById(R.id.representativeFb);
        twitter = getActivity().findViewById(R.id.representativeTwitter);
        office = getActivity().findViewById(R.id.representativeOffice);
        website = getActivity().findViewById(R.id.representativeWebsite);

        name.setText(representative.getName());
        party.setText(representative.getParty());
        committee.setText(representative.getCommittee());
        years.setText(representative.getYears());
        fb.setText(representative.getFb());
        twitter.setText(representative.getTwitter());
        office.setText(representative.getOffice().toString());
        website.setText(representative.getWebsite());

        if (representative.getOffice() == Offices.PRESIDENT || representative.getOffice() == Offices.VICE_PRESIDENT){
            committee.setVisibility(View.GONE);
            getActivity().findViewById(R.id.committee).setVisibility(View.GONE);
        }

        int radius = 40;
        int margin = 0;
        Glide.with(getContext()).load(representative.getProfileImage()).placeholder(R.drawable.officials).transform(new RoundedCornersTransformation(radius, margin)).into(image);


        final FragmentManager fragmentManager = getChildFragmentManager();
        bottomNavigationView = getActivity().findViewById(R.id.votingHistoryNavigation);
        final Fragment gunVotingHistory = new GunVotingHistoryFragment();
        final Fragment taxesVotingHistory = new TaxesVotingHistoryFragment();


        //set up bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.climateControlIcon:
                        fragment = gunVotingHistory;
                        break;
                    case R.id.taxesIcon:
                        fragment = taxesVotingHistory;
                        break;
                    default:
                        fragment = gunVotingHistory;
                        break;
                }
//                //pass user data with fragment
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("user", Parcels.wrap(user));
//                fragment.setArguments(bundle);

                //set fragment
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.climateControlIcon);
    }
}