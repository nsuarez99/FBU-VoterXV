package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.ElectionAdapter;
import com.example.fbu_voterxv.adapters.PoliticalViewAdapter;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.PoliticalView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.List;
import java.util.Map;

public class PoliticalViewFragment extends Fragment {

    public static final String TAG = "PoliticalViewFragment";
    private RecyclerView recyclerView;
    private Candidate candidate;
    private List<PoliticalView> politicalViews;
    private PoliticalViewAdapter adapter;

    public PoliticalViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_political_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        candidate = Parcels.unwrap(getArguments().getParcelable("candidate"));
        politicalViews = Parcels.unwrap(getArguments().getParcelable("views"));

        recyclerView = getActivity().findViewById(R.id.politicalViewRecycler);

        //set adapter and recycler view
        adapter = new PoliticalViewAdapter(getContext(), politicalViews);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}