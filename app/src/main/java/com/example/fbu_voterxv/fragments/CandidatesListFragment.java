package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.CandidateListAdapter;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;

import org.parceler.Parcels;

import java.util.List;

public class CandidatesListFragment extends Fragment {

    private RecyclerView recyclerView;
    private Election election;
    private List<Candidate> candidates;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private CandidateListAdapter adapter;

    public CandidatesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_candidatelist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get candidates data
        election = Parcels.unwrap(getArguments().getParcelable("election"));
        candidates = election.getCandidates();

        //set fragmentManager and fragment
        fragmentManager = getActivity().getSupportFragmentManager();
        fragment = new CandidateFragment();

        recyclerView = getActivity().findViewById(R.id.candidatesRecyclerView);

        //set on click listener
        CandidateListAdapter.OnClickListener onClickListener = new CandidateListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Candidate candidate = candidates.get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable("candidate", Parcels.wrap(candidate));
                fragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.frameLayoutContainer, fragment).commit();
            }
        };

        //set adapter and recycler view
        adapter = new CandidateListAdapter(getContext(), candidates, onClickListener);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

    }
}