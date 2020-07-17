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

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.ElectionAdapter;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ElectionsFragment extends Fragment {

    public static final String TAG = "ElectionsFragment";
    private RecyclerView recyclerView;
    private List<Election> elections;
    private ElectionAdapter adapter;
    private User user;

    public ElectionsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elections, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = Parcels.unwrap(getArguments().getParcelable("user"));

        recyclerView = view.findViewById(R.id.recyclerView);

        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final Fragment candidatesFragment = new CandidatesListFragment();

        //setup recyclerview onclick listener
        ElectionAdapter.OnClickListener onClickListener = new ElectionAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Election election = elections.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("election", Parcels.wrap(election));
                candidatesFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, candidatesFragment).commit();
            }
        };

        //set adapter and recycler view
        elections = user.getElectionsList();
        adapter = new ElectionAdapter(getContext(), elections, onClickListener);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }
}