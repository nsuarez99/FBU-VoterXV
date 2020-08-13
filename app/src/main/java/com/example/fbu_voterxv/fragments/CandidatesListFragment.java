package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.adapters.CandidateListAdapter;
import com.example.fbu_voterxv.models.Candidate;
import com.example.fbu_voterxv.models.Election;

import org.parceler.Parcels;

import java.util.List;

public class CandidatesListFragment extends Fragment {

    public static final String TAG = "CandidatesListFragment";
    private RecyclerView recyclerView;
    private Election election;
    private List<Candidate> candidates;
    private FragmentManager fragmentManager;
    private Fragment fragment;
    private CandidateListAdapter adapter;
    private GestureDetector gestureDetector;
    private static final int SWIPE_MIN_DISTANCE = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

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
                fragmentManager.beginTransaction().replace(R.id.layoutContainer, fragment).addToBackStack(null).commit();
            }
        };

        //set adapter and recycler view
        adapter = new CandidateListAdapter(getContext(), candidates, onClickListener);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

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

        //set gesture recognizer
        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        recyclerView.setOnTouchListener(touchListener);

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
            if (event1 == null || event2 == null){
                return false;
            }
            if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                Log.d(TAG, "onFling: ");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
                return true;
            }
            return false;
        }
    }
}