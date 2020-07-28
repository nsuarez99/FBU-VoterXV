package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.VotingHistoryAdapter;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.RollCall;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VotingHistoryFragment extends Fragment {

    public static final String TAG = "VotingHistoryFragment";
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
        Log.i(TAG, "createview");
        return inflater.inflate(R.layout.fragment_voting_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "view created");

        representative = Parcels.unwrap(getArguments().getParcelable("rep"));
        Set<Bill> billsSet = (Set<Bill>) Parcels.unwrap(getArguments().getParcelable("bills"));
        bills = getRepresentativeBills(new ArrayList<Bill>(billsSet));

        votingRecyclerView = view.findViewById(R.id.votingHistoryRecyclerView);

        //set adapter and recycler view
        adapter = new VotingHistoryAdapter(getContext(), bills, representative);
        votingRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        votingRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private List<Bill> getRepresentativeBills(List<Bill> bills){
        List<Bill> newList = new ArrayList<>();
        for (Bill bill: bills) {
            RollCall rollCall;
            if (representative.getOffice() == Offices.SENATE) {
                rollCall = bill.getSenateRollCall();
            } else if (representative.getOffice() == Offices.HOUSE_OF_REPRESENTATIVES) {
                rollCall = bill.getHouseRollCall();
            } else {
                return newList;
            }

            //chamber has not voted on this bill
            if (rollCall == null){
                continue;
            }

            Map<Representative, String> votes = rollCall.getVotes();
            for (Representative rep : votes.keySet()) {
                if (rep.equals(representative)) {
                    newList.add(bill);
                }
            }
        }
        return newList;
    }
}