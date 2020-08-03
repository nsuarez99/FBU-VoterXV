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
    private GestureDetector gestureDetector;

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
            Log.d(TAG, "onFling: ");
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStackImmediate();
            return true;
        }
    }
}