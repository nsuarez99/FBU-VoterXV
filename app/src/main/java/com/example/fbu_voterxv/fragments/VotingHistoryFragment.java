package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.ElectionAdapter;
import com.example.fbu_voterxv.adapters.VotingHistoryAdapter;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.Representative;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.List;
import java.util.Map;

public class VotingHistoryFragment extends Fragment {

    public static final String TAG = "RepresentativeFragment";
    private List<Bill> bills;
    private Representative representative;
    private RecyclerView votingRecyclerView;
    private VotingHistoryAdapter adapter;

    public VotingHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voting_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get bill data
        bills = Parcels.unwrap(getArguments().getParcelable("bills"));
        representative = Parcels.unwrap(getArguments().getParcelable("rep"));


        votingRecyclerView = view.findViewById(R.id.votingHistoryRecyclerView);

        //set adapter and recycler view
        adapter = new VotingHistoryAdapter(getContext(), bills, representative);
        votingRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        votingRecyclerView.setLayoutManager(linearLayoutManager);
    }
}