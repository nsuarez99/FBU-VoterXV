package com.example.fbu_voterxv.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.PoliticalView;
import com.example.fbu_voterxv.models.Candidate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CandidateFragment extends Fragment {

    private ImageView image;
    private TextView name;
    private TextView party;
    private TextView moneyRaised;
    private TextView slogan;
    private TextView office;
    private Candidate candidate;
    private BottomNavigationView bottomNavigationView;
    private Map<String, List<PoliticalView>> views;


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
        views = createMockData(candidate.getParty());

        image = getActivity().findViewById(R.id.candidateImage);
        name = getActivity().findViewById(R.id.candidateName);
        party = getActivity().findViewById(R.id.candidateParty);
        moneyRaised = getActivity().findViewById(R.id.candidateMoneyRaised);
        slogan = getActivity().findViewById(R.id.candidateSlogan);
        office = getActivity().findViewById(R.id.candidateOffice);
        bottomNavigationView = getActivity().findViewById(R.id.politicalViewsNavigation);

        int radius = 40;
        int margin = 0;
        Glide.with(getContext()).load(candidate.getProfileImage()).placeholder(R.drawable.politician).transform(new RoundedCornersTransformation(radius, margin)).into(image);
        name.setText(candidate.getName());
        party.setText(candidate.getParty());
        moneyRaised.setText(candidate.getMoney_raisedString());
        slogan.setText(candidate.getSlogan());
        office.setText(candidate.getOffice().toString());

        final FragmentManager fragmentManager = getChildFragmentManager();

        //set up bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                Bundle bundle = new Bundle();
                switch (menuItem.getItemId()) {
                    case R.id.defensePoliticalIcon:
                        fragment = new PoliticalViewFragment();
                        bundle.putParcelable("views", Parcels.wrap(views.get("defense")));
                        break;
                    case R.id.healthPoliticalIcon:
                        fragment = new PoliticalViewFragment();
                        bundle.putParcelable("views", Parcels.wrap(views.get("health")));
                        break;
                    case R.id.educationPoliticalIcon:
                        fragment = new PoliticalViewFragment();
                        bundle.putParcelable("views", Parcels.wrap(views.get("education")));
                        break;
                    case R.id.socialPoliticalIcon:
                        fragment = new PoliticalViewFragment();
                        bundle.putParcelable("views", Parcels.wrap(views.get("social")));
                        break;
                    default:
                        fragment = new PoliticalViewFragment();
                        bundle.putParcelable("views", Parcels.wrap(views.get("economy")));
                        break;
                }
                //pass user data with fragment
                bundle.putParcelable("candidate", Parcels.wrap(candidate));
                fragment.setArguments(bundle);

                //set fragment
                fragmentManager.beginTransaction().replace(R.id.politicalViewsLayoutContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.defensePoliticalIcon);

    }


    /**
     * creates mock data on key issues based on party
     * @param party candidate party
     * @return list of PoliticalView for each key issue
     */
    private Map<String, List<PoliticalView>> createMockData(String party){
        List<PoliticalView> defenseViews = new ArrayList<>();
        List<PoliticalView> economyViews = new ArrayList<>();
        List<PoliticalView> healthViews = new ArrayList<>();
        List<PoliticalView> socialViews = new ArrayList<>();
        List<PoliticalView> educationViews = new ArrayList<>();

        Map<String, List<PoliticalView>> allViews = new HashMap<>();

        allViews.put("defense", defenseViews);
        allViews.put("economy", economyViews);
        allViews.put("health", healthViews);
        allViews.put("social", socialViews);
        allViews.put("education", educationViews);

        if (party.equals("Republican")){
            PoliticalView guns = new PoliticalView("Gun Ownership", "Lawful gun ownership enables Americans to exercise their God-given right of self-defense for the safety of their homes, " +
                    "their loved ones, and their communities. ... We oppose ill-conceived laws that would restrict magazine capacity or ban the sale of the most popular and common modern rifle");
            PoliticalView immigration = new PoliticalView("Immigration", "Our highest priority ... must be to secure our borders and all ports of entry and to enforce our immigration laws");
            defenseViews.add(guns);
            defenseViews.add(immigration);

            PoliticalView taxes = new PoliticalView("Taxes", "Wherever tax rates penalize thrift or discourage investment, they must be lowered. " +
                    "Wherever current provisions of the code are disincentives for economic growth, they must be changed");
            PoliticalView trade = new PoliticalView("Trade", "We envision a worldwide multilateral agreement among nations committed to the principles of open markets");
            economyViews.add(taxes);
            economyViews.add(trade);

            PoliticalView judges = new PoliticalView("Judicial Appointments", "We ... support the appointment of justices and judges who respect the constitutional " +
                    "limits on their power and respect the authority of the states to decide ... fundamental social questions");
            socialViews.add(judges);

            PoliticalView medicare = new PoliticalView("Medicare and Medicaid", "To preserve Medicare and Medicaid, the financing of these important programs must be " +
                    "brought under control before they consume most of the federal budget, including national defense");
            PoliticalView health = new PoliticalView("Health Care", "[The Affordable Care Act] must be removed and replaced with an approach based on genuine competition, patient choice, excellent care, wellness, and timely access to treatment");
            healthViews.add(health);
            healthViews.add(medicare);

            PoliticalView education = new PoliticalView("Education", "Support school choice through charter schools and school vouchers for private schools");
            educationViews.add(education);
        }
        else{
            PoliticalView guns = new PoliticalView("Gun Ownership", "Expand and strengthen background checks and close dangerous loopholes in our current laws concerning firearms");
            defenseViews.add(guns);

            PoliticalView taxes = new PoliticalView("Taxes", "Ensure those at the top contribute to our country’s future by establishing a multimillionaire surtax to ensure millionaires and billionaires pay their fair share");
            economyViews.add(taxes);

            PoliticalView social = new PoliticalView("Individual Rights", "We will seek to safeguard vulnerable minorities, including LGBT people and people with disabilities");
            socialViews.add(social);

            PoliticalView medicare = new PoliticalView("Public Health", "Americans should be able to access public coverage through a public option, and those over 55 should be able to opt in to Medicare");
            PoliticalView health = new PoliticalView("Women's Health", "Every woman should have access to quality reproductive health care services, including safe and legal abortion");
            healthViews.add(medicare);
            healthViews.add(health);

            PoliticalView education = new PoliticalView("Education", "Favor improving public education by raising school standards and reforming the Head Start program. " +
                    "Also support universal preschool and expanding access to primary education while also calling for slashes in student loan debt and support reforms to force down tuition fees.");
            educationViews.add(education);
        }
        return allViews;
    }

}