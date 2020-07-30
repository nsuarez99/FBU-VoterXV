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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class RepresentativeFragment extends Fragment {

    public static final String TAG = "RepresentativeFragment";
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
    private Map<String, List<Bill>> bills;


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
        bills = Parcels.unwrap(getArguments().getParcelable("bills"));


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


        //if vice president then dont show voting
        if (representative.getOffice() == Offices.VICE_PRESIDENT){
           bottomNavigationView.setVisibility(View.GONE);
        }
        else{
            //set up bottom navigation
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment;
                    Bundle bundle = new Bundle();
                    switch (menuItem.getItemId()) {
                        case R.id.defenseIcon:
                            fragment = new VotingHistoryFragment();
                            bundle.putParcelable("bills", Parcels.wrap(bills.get("defense")));
                            break;
                        case R.id.healthIcon:
                            fragment = new VotingHistoryFragment();
                            bundle.putParcelable("bills", Parcels.wrap(bills.get("health")));
                            break;
                        case R.id.educationIcon:
                            fragment = new VotingHistoryFragment();
                            bundle.putParcelable("bills", Parcels.wrap(bills.get("education")));
                            break;
                        case R.id.socialIcon:
                            fragment = new VotingHistoryFragment();
                            bundle.putParcelable("bills", Parcels.wrap(bills.get("social")));
                            break;
                        default:
                            fragment = new VotingHistoryFragment();
                            bundle.putParcelable("bills", Parcels.wrap(bills.get("economy")));
                            break;
                    }
                    //pass user data with fragment
                    bundle.putParcelable("rep", Parcels.wrap(representative));
                    fragment.setArguments(bundle);

                    //set fragment
                    fragmentManager.beginTransaction().replace(R.id.votingHistoryLayoutContainer, fragment).commit();
                    return true;
                }
            });
            bottomNavigationView.setSelectedItemId(R.id.economyIcon);
        }

    }
}