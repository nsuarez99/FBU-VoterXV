package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.VotingHistoryAdapter;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.RollCall;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VotingHistoryFragment extends Fragment {

    public static final String TAG = "VotingHistoryFragment";
    private RecyclerView votingRecyclerView;
    private TextView score;
    private VotingHistoryAdapter adapter;
    private List<Bill> bills;
    private Representative representative;
    private Map<Bill, String> votes;
    private GestureDetector gestureDetector;


    public VotingHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voting_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "view created");

        representative = Parcels.unwrap(getArguments().getParcelable("rep"));
        Set<Bill> billsSet = (Set<Bill>) Parcels.unwrap(getArguments().getParcelable("bills"));

        votingRecyclerView = view.findViewById(R.id.votingHistoryRecyclerView);
        score = view.findViewById(R.id.votingScore);

        //set votes map and bills list
        votes = setVotes(billsSet);
        bills = new ArrayList<>(votes.keySet());

        //set voting category of representative
        double votingScore = votingScore(votes, representative.getOffice());
        Log.i(TAG, "" + votingScore);
        score.setText("Voting as a: " + votingCategory(votingScore));

        //set adapter and recycler view
        adapter = new VotingHistoryAdapter(getContext(), bills, representative, votes);
        votingRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        votingRecyclerView.setLayoutManager(linearLayoutManager);

        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass the events to the gesture detector
                // a return value of true means the detector is handling it
                // a return value of false means the detector didn't
                // recognize the event
                return gestureDetector.onTouchEvent(event);

            }
        };

        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        votingRecyclerView.setOnTouchListener(touchListener);

    }

    /**
     * creates new map with just bills that the representative was present for
     * @param bills set of all bills in subject
     * @return maps the representatives vote to the bill
     */
    public Map<Bill, String> setVotes(Set<Bill> bills){
        Map<Bill, String> newVotes = new HashMap<>();
        for (Bill bill: bills) {
            RollCall rollCall;
            if (representative.getOffice() == Offices.SENATE) {
                rollCall = bill.getSenateRollCall();
            } else if (representative.getOffice() == Offices.HOUSE_OF_REPRESENTATIVES) {
                rollCall = bill.getHouseRollCall();
            } else {
                if (bill.getLaw() != null ){
                    newVotes.put(bill, "Yes");
                }
                else if (bill.getVeto() != null){
                    newVotes.put(bill, "No");
                }
                continue;
            }

            //chamber has not voted on this bill
            if (rollCall == null){
                continue;
            }

            Map<Representative, String> allVotes = rollCall.getVotes();
            for (Representative rep : allVotes.keySet()) {
                if (rep.equals(representative)) {
                    newVotes.put(bill, allVotes.get(rep));
                }
            }
        }
        return newVotes;
    }

    /**
     * takes rep voting score and then returns the political category that score is
     * @param score votingScore of rep
     * @return string of political category rep belongs to
     */
    public String votingCategory(double score){
        if (score >= 0.85){
            return "Extreme Democrat";
        }
        else if (score >= 0.55){
            return "Progressive Democrat";
        }
        else if (score >= .25){
            return "Moderate Democrat";
        }
        else if (score >= .10){
            return "Conservative Democrat";
        }
        else if (score >= -0.1){
            return "Independent";
        }
        else if (score >= -0.25){
            return "Liberal Republican";
        }
        else if (score >= -0.55){
            return "Moderate Republican";
        }
        else if (score >= -0.85){
            return "Conservative Republican";
        }
        else{
            return "Extreme Republican";
        }
    }

    /**
     * Adds up all the sponsors and cosponsors of a bill then creates a score (look at  for that bill then adds up all scores and divides by total number
     * of bills to get final score for bills
     * @param bills bills of representative
     * @param offices office of the representative
     * @return decimal of how conservative or liberal rep is based on votes of bills (1 being super liberal, -1 being super conservative)
     */
    public double votingScore(Map<Bill, String> bills, Offices offices){
        double totalScore = 0;
        int billsSize = bills.size();
        List<String> votedAgainstParty = new ArrayList<>();
        for (Bill bill : bills.keySet()){
            Map<RollCall, Integer> rollCallMap = new HashMap<>();

            //if signed bill then ignore but if veto then calculate score
            if (offices == Offices.PRESIDENT || offices == Offices.VICE_PRESIDENT){
                if (bills.get(bill).equals("Yes")){
                    billsSize -= 1;
                    continue;
                }
            }

            if (offices == Offices.HOUSE_OF_REPRESENTATIVES || offices == Offices.PRESIDENT){
                rollCallMap.put(bill.getHouseRollCall(), 435);
            }
            if (offices == Offices.SENATE || offices == Offices.PRESIDENT){
                rollCallMap.put(bill.getSenateRollCall(), 100);
            }

            for (RollCall rollCall : rollCallMap.keySet()){
                int totalVotes = rollCallMap.get(rollCall);

                //if one of chamber votes was a voice vote and not rollcall
                if (rollCall == null){
                    continue;
                }

                //get breakdown of sponsors and cosponsors by party
                Map<String, Integer> cosponsors = bill.getCosponsors();
                int democrats = cosponsors.get("D");
                int republicans = cosponsors.get("R");
                int independents = cosponsors.get("ID");
                String sponsorParty = bill.getSponsor().getParty();
                if (sponsorParty.equals("Democrat")){
                    democrats += 1;
                }
                else if (sponsorParty.equals("Republican")){
                    republicans += 1;
                }
                else{
                    independents += 1;
                }
                final double totalSponsors = democrats + independents + republicans;

                //if over 85% of the chamber voted for the bill then score is 0 (neutral) unless voted against then adds bill to list
                boolean massVote = false;
                double totalPercentYes = rollCall.getTotalBreakdown().get("yes") / totalVotes;
                if (totalPercentYes > 0.85){
                    massVote = true;
                }

                //set political score
                double score = politicalScale(democrats / totalSponsors * 100);
                String vote = bills.get(bill);
                if (vote.equals("Yes") && massVote){
                    score = 0;
                }
                else if (vote.equals("No")){
                    if (massVote){
                        votedAgainstParty.add(sponsorParty);
                        score = 0;
                        billsSize -= 1;
                    }
                    else{
                        score = -score;
                    }
                }
                else if (vote.equals("Not Voting")){
                    score = 0;
                    billsSize -= 1;
                }

                totalScore += score;
            }
        }

        //calculate total voting average
        double average = totalScore / billsSize;
        int averageSize = 1;
        if (billsSize == 0){
            average = 0;
            averageSize = 0;
        }

        //for every vote against if polarizes rep more to their extreme
        if (representative.getParty().equals("Democrat")){
            average += votedAgainstParty.size();
        }
        else if (representative.getParty().equals("Republican")){
            average -= votedAgainstParty.size();
        }
        else {
            for (String party : votedAgainstParty) {
                if (party.equals("Democrat")) {
                    average += -1;
                }
                if (party.equals("Republican")) {
                    average += 1;

                }
                else{
                    average += average;
                }
            }
        }

        int totalSize = votedAgainstParty.size() + averageSize;
        if (totalSize == 0){
            return average;
        }
        return average / (votedAgainstParty.size() + averageSize);
    }

    /**
     * returns decimal of how conservative or liberal the bill is based on the percentage of democrats that wrote the bill
     * @param percentage percentage of democrats that wrote the bill
     * @return decimal of how conservative or liberal the bill
     */
    private double politicalScale(double percentage){
        if (percentage >= 88.75){
            return 1;
        }
        else if (percentage >= 77.5){
            return 0.75;
        }
        else if (percentage >= 66.25){
            return 0.5;
        }
        else if (percentage >= 55){
            return 0.25;
        }
        else if (percentage >= 45){
            return 0;
        }
        else if (percentage >= 33.75){
            return -0.25;
        }
        else if (percentage >= 22.50){
            return -0.5;

        }
        else if (percentage >= 11.25){
            return -0.75;
        }
        else{
            return -1;
        }

    }

    // In the SimpleOnGestureListener subclass you should override
    // onDown and any other gesture that you want to detect.
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(TAG, "onFling: ");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStackImmediate();
            return true;
        }
    }

}