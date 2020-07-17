package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Representative;

import org.parceler.Parcels;

public class CandidateFragment extends Fragment {

    private ImageView image;
    private TextView name;
    private TextView party;
    private TextView moneyRaised;
    private TextView slogan;
    private TextView fb;
    private TextView twitter;
    private TextView office;
    private TextView website;
    private Candidate candidate;

    public CandidateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_candidate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        candidate = Parcels.unwrap(getArguments().getParcelable("candidate"));

        image = getActivity().findViewById(R.id.candidateImage);
        name = getActivity().findViewById(R.id.candidateName);
        party = getActivity().findViewById(R.id.candidateParty);
        moneyRaised = getActivity().findViewById(R.id.candidateMoneyRaised);
        slogan = getActivity().findViewById(R.id.candidateSlogan);
        fb = getActivity().findViewById(R.id.candidateFb);
        twitter = getActivity().findViewById(R.id.candidateTwitter);
        office = getActivity().findViewById(R.id.candidateOffice);
        website = getActivity().findViewById(R.id.candidateWebsite);

        Glide.with(getContext()).load(candidate.getProfileImage()).placeholder(R.drawable.officials).into(image);
        name.setText(candidate.getName());
        party.setText(candidate.getParty());
        moneyRaised.setText(candidate.getMoney_raisedString());
        slogan.setText(candidate.getSlogan());
        fb.setText(candidate.getFb());
        twitter.setText(candidate.getTwitter());
        office.setText(candidate.getOffice().toString());
        website.setText(candidate.getWebsite());


    }
}