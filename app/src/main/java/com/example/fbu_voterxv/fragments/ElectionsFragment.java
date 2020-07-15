package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.ElectionAdapter;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ElectionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Election> elections;
    private ElectionAdapter adapter;

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
        elections = new ArrayList<>();
        adapter = new ElectionAdapter(getContext(), elections, onClickListener);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //TODO get rid of test election
        List<Candidate> candidates = new ArrayList<>();
        candidates.add(new Candidate("Test name", 99, "https://i.pinimg.com/originals/80/8c/65/808c65ecb9ecce89e5b52cff5f45af5e.png", "Democrat", "gender", "website", "fb", "twitter", "money_raised"));
        candidates.add(new Candidate("Test name 2", 99, "https://i.pinimg.com/originals/80/8c/65/808c65ecb9ecce89e5b52cff5f45af5e.png", "Democrat", "gender", "website", "fb", "twitter", "money_raised"));
        elections.add(new Election("test election", "0/0/0000", candidates));
        adapter.addAll(elections);

        //TODO add endless scrolling
//        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                loadNextQueryPosts(elections.size());
//            }
//        };
//        recyclerView.addOnScrollListener(scrollListener);

    }
}