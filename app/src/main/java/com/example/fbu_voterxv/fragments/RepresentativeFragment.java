package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Representative;

import org.parceler.Parcels;
import org.w3c.dom.Text;

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

        Glide.with(getContext()).load(representative.getProfileImage()).placeholder(R.drawable.officials).into(image);
        name.setText(representative.getName());
        party.setText(representative.getParty());
        committee.setText(representative.getCommittee());
        years.setText(representative.getYears());
        fb.setText(representative.getFb());
        twitter.setText(representative.getTwitter());
        office.setText(representative.getOffice().toString());
        website.setText(representative.getWebsite());


    }
}