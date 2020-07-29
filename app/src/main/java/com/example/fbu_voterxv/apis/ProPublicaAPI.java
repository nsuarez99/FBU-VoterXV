package com.example.fbu_voterxv.apis;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.fbu_voterxv.BuildConfig;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.MyOfficials;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.RollCall;
import com.example.fbu_voterxv.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Headers;

public class ProPublicaAPI {

    public static final String TAG = "ProPublicaAPI";
    private static final String BASE_URL = "https://api.propublica.org/congress/v1/";
    private static final String KEY = BuildConfig.PROPPUBLICA_KEY;


    public static class OfficialsBasicParse{

        //sets senators, congressman years, committees, party
        public static void setRepBasicInfo(final User user) {
            setCongressmanInfo(user);
            setSenatorsInfo(user);
        }

        private static void setSenatorsInfo(final User user){
            String chamber = "senate";
            String state = user.getState();
            String arguments = String.format("members/%s/%s/current.json", chamber, state);
            final String URL = BASE_URL + arguments;

            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess senateYears");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseReps(jsonArray, user.getOfficials().getJuniorSenator());
                        parseReps(jsonArray, user.getOfficials().getSeniorSenator());
                        setJuniorSeniorSenator(user.getOfficials());
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure senatorYears: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }

        private static void setCongressmanInfo(final User user){
            String chamber = "house";
            String state = user.getState();
//            String district = user.getDistrict();
            String arguments = String.format("members/%s/%s/current.json", chamber, state);
            final String URL = BASE_URL + arguments;

            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess congressmanYears");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseReps(jsonArray, user.getOfficials().getCongressman());
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure congressmanYears: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }

        //setting the correct order of junior and senior senators
        private static void setJuniorSeniorSenator(MyOfficials myOfficials){
            Representative seniorSenator = myOfficials.getSeniorSenator();
            Representative juniorSenator = myOfficials.getJuniorSenator();
            int juniorYears = Integer.parseInt(juniorSenator.getYears().substring(juniorSenator.getYears().length() - 4));
            int seniorYears = Integer.parseInt(seniorSenator.getYears().substring(seniorSenator.getYears().length() - 4));

            if (juniorYears < seniorYears){
                myOfficials.setJuniorSenator(seniorSenator);
                myOfficials.setSeniorSenator(juniorSenator);
            }

        }

        //parse fromJson Object party, committee, years
        private static void parseReps(JSONArray jsonArray, Representative representative) throws JSONException {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (representative.nameEquals(jsonObject.getString("name"))){

                    //set years in office
                    int years = Integer.parseInt(jsonObject.getString("seniority"));
                    String electionYear = jsonObject.getString("next_election");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy");
                    Date date = new Date();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR, years * -1);
                    String startYear = format.format(calendar.getTime());
                    representative.setYears(startYear + " - " + electionYear);

                    //set party
                    String party = jsonObject.getString("party");
                    representative.setParty(parseParty(party));

                    //set committees
                    String id = jsonObject.getString("id");
                    getCommittees(id, representative);
                    return;
                }
            }
        }

        private static String parseParty(String party){
            if (party.equals("I")){
                return "Independent";
            }
            else if (party.equals("D")){
                return "Democrat";
            }
            if (party.equals("R")){
                return "Republican";
            }
            if (party.equals("L")){
                return "Libertarian";
            }
            return party;
        }

        private static void getCommittees(String id, final Representative representative){
            final String URL = BASE_URL + "members/" + id + ".json";

            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess repCommittee: " + representative.getName());
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseCommittee(jsonArray, representative);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure repCommittee: %s \nstatusCode:%d \nresponse:%s", representative.getName(), statusCode, response));
                }
            });
        }

        private static void parseCommittee(JSONArray jsonArray, Representative representative) throws JSONException {
            if (jsonArray.length() > 1) {
                Log.e(TAG, "more than 1 result for representative: "  + jsonArray);
                return;
            }
            else if (jsonArray.length() == 0){
                Log.e(TAG, "0 result for representative: " + jsonArray);
                return;
            }

            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject latestCongress = jsonObject.getJSONArray("roles").getJSONObject(0);
            JSONArray committees = latestCongress.getJSONArray("committees");
            String committeeString = "";
                for (int i = 0; i < committees.length() ; i++) {
                    JSONObject committee = committees.getJSONObject(i);
                    committeeString += committee.getString("name") + ", ";
                }
            if (committees.length() != 0){
                committeeString = committeeString.substring(0, committeeString.length() - 2);
                representative.setCommittee(committeeString);
            }
            else{
                representative.setCommittee("None");
            }
        }
    }


    public static class OfficialsVotingParse{

        public static  Map<String, Set<Bill>> getBills(Map<String, Set<Bill>> bills){
            Set<Bill> defenseBills = new HashSet<>();
            Set<Bill> economyBills = new HashSet<>();
            Set<Bill> healthBills = new HashSet<>();
            Set<Bill> socialBills = new HashSet<>();
            Set<Bill> educationBills = new HashSet<>();


            bills.put("defense", defenseBills);
            bills.put("economy", economyBills);
            bills.put("health", healthBills);
            bills.put("social", socialBills);
            bills.put("education", educationBills);


            Set<String> defenseSubjects = new HashSet<>(Arrays.asList("homeland-security", "law-enforcement-administration-and-funding",
                    "law-enforcement-officers", "military-procurement-research-weapons-development", "military-readiness", "terrorism",
                    "veterans-education-employment-rehabilitation", "veterans-loans-housing-homeless-programs",
                    "veterans-organizations-and-recognition", "veterans-pensions-and-compensation"));
            for (String subject: defenseSubjects) {
                getSubjectBills(subject, defenseBills, 0);
            }

            Set<String> immigrationSubjects = new HashSet<>(Arrays.asList("border-security-and-unlawful-immigration","immigration-status-and-procedures","immigrant-health-and-welfare"));
            for (String subject: immigrationSubjects) {
                getSubjectBills(subject, defenseBills, 0);
            }
//
            Set<String> gunSubjects = new HashSet<>(Arrays.asList("firearms-and-explosives", "violent-crime"));
            for (String subject: gunSubjects) {
                getSubjectBills(subject, defenseBills, 0);
            }
//
            Set<String> economySubject = new HashSet<>(Arrays.asList("capital-gains-tax","economic-development","employment-taxes","free-trade-and-trade-barriers",
                    "general-taxation-matters","income-tax-credits","income-tax-deductions","income-tax-deferral","income-tax-exclusion","income-tax-rates", "minority-employment",
                    "infrastructure-development","normal-trade-relations-most-favored-nation-treatment","property-tax","social-security-and-elderly-assistance",
                    "tax-reform-and-tax-simplification","trade-agreements-and-negotiations","trade-restrictions","transfer-and-inheritance-taxes",
                    "urban-and-suburban-affairs-and-development","wages-and-earnings","womens-employment"));
            for (String subject: economySubject) {
                getSubjectBills(subject, economyBills, 0);
            }

            Set<String> healthSubjects = new HashSet<>(Arrays.asList("abortion","comprehensive-health-care","drug-alcohol-tobacco-use","family-planning-and-birth-control",
                    "general-health-and-health-care-finance-matters","health-care-costs-and-insurance","health-care-coverage-and-access","health-care-quality", "minority-health",
                    "medicaid","medicare","prescription-drugs","religion-and-medicine","sex-and-reproductive-health","teenage-pregnancy","womens-health"));
            for (String subject: healthSubjects) {
                getSubjectBills(subject, healthBills, 0);
            }

            Set<String> socialSubjects = new HashSet<>(Arrays.asList("criminal-procedure-and-sentencing","due-process-and-equal-protection",
                    "employment-discrimination-and-employee-rights","first-amendment-rights","hate-crimes","racial-and-ethnic-relations","right-of-privacy",
                    "public-housing","sex-gender-sexual-orientation-discrimination","voting-rights","womens-rights"));
            for (String subject: socialSubjects) {
                getSubjectBills(subject, socialBills, 0);
            }

            Set<String> education = new HashSet<>(Arrays.asList("child-care-and-development","education-programs-funding","educational-facilities-and-institutions","minority-education",
                    "educational-guidance","general-education-matters","higher-education","preschool-education","religion-in-the-public-schools","school-administration",
                    "science-and-engineering-education","student-aid-and-college-costs","teaching-teachers-curricula","vocational-and-technical-education","womens-education"));
            for (String subject: education) {
                getSubjectBills(subject, educationBills, 0);
            }

            return bills;
        }

        private static void getSubjectBills(final String subject, final Set<Bill> bills, int offset){
            String arguments = String.format("bills/subjects/%s.json", subject);
            final String URL = BASE_URL + arguments;

            RequestParams params = new RequestParams();
            params.put("offset", offset);
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess getSubjectBills");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseBills(jsonArray, bills);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e + "\n Subject: " + subject + "\n" + jsonObject);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure getSubjectBills: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }

        private static void parseBills(JSONArray jsonArray, Set<Bill> bills) throws JSONException {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (!jsonObject.getString("last_vote").equals("null")){
                    Bill bill = new Bill();

                    bill.setSlugID(jsonObject.getString("bill_slug"));
                    bill.setTitle(jsonObject.getString("short_title"));
                    bill.setBriefSummary(jsonObject.getString("title"));
                    bill.setSummary(jsonObject.getString("summary"));
                    bill.setUrl(jsonObject.getString("congressdotgov_url"));
                    bill.setSubject(jsonObject.getString("primary_subject"));
                    bill.setLastAction(parseDate(jsonObject.getString("latest_major_action_date")));
                    bill.setSponsor(parseSponsor(jsonObject));
                    bill.setCosponsors(parseCosponsors(jsonObject.getJSONObject("cosponsors_by_party")));

                    String id = jsonObject.getString("bill_id");
                    int dashIndex = id.indexOf("-");
                    bill.setCongress(id.substring(dashIndex + 1));

                    bills.add(bill);
                    getSpecificBill(bill);
                }
            }
        }

        private static Date parseDate(String rawJsonTime){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try{
                date = dateFormat.parse(rawJsonTime);
            } catch (ParseException e) {
                Log.e(TAG, "error parsing date");
                e.printStackTrace();
            }
            return date;
        }

        private static Map<String, Integer> parseCosponsors(JSONObject jsonObject) throws JSONException {
            Map<String, Integer> cosponsors = new HashMap<>();
            cosponsors.put("D", 0);
            cosponsors.put("R", 0);
            cosponsors.put("ID", 0);

            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext() ) {
                String sponsorParty = (String)keys.next();
                int numberOfSponsor = jsonObject.getInt(sponsorParty);
                cosponsors.put(sponsorParty, numberOfSponsor);
            }
            return  cosponsors;
        }

        private static Representative parseSponsor(JSONObject jsonObject) throws JSONException {
            Representative representative = new Representative();
            String name = jsonObject.getString("sponsor_name");
            String state = jsonObject.getString("sponsor_state");
            String party = jsonObject.getString("sponsor_party");
            String title = jsonObject.getString("sponsor_title");

            representative.setName(name);
            representative.setState(state);

            Offices offices;
            if (title.equals("Rep.")){
                offices = Offices.HOUSE_OF_REPRESENTATIVES;
            }
            else{
                offices = Offices.SENATE;
            }
            representative.setOffice(offices);

            if (party.equals("D")){
                representative.setParty("Democrat");
            }
            else if(party.equals("R")){
                representative.setParty("Republican");
            }
            else{
                representative.setParty("Independent");
            }
            return representative;
        }

        private static void getSpecificBill(final Bill bill){
            String arguments = String.format("%s/bills/%s.json", bill.getCongress(), bill.getSlugID());
            final String URL = BASE_URL + arguments;

            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess getSpecificBills");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        parseSpecificBill(jsonArray, bill);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure getSpecificBills: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });
        }

        private static void parseSpecificBill(JSONArray jsonArray, Bill bill) throws JSONException {
            if (jsonArray.length() > 1){
                Log.e(TAG, "more than one specific bill");
            }
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            //TODO implement veto
//            String veto = jsonObject.getString("vetoed");
//            if (houseVote != "null"){
//                bill.setHouse_vote(true);
//            }
//            if (senateVote != "null"){
//                bill.setHouse_vote(true);
//            }
//            if (veto != "null"){
//                bill.setHouse_vote(true);
//            }


            JSONArray actions = jsonObject.getJSONArray("votes");
            parseSpecificBillActions(actions, bill);


        }

        private static void parseSpecificBillActions(JSONArray jsonArray, Bill bill) throws JSONException {
            List<String> votingMotions = new ArrayList<>(Arrays.asList("On Agreeing to the Conference Report", "On the Joint Resolution", "On Passage", "On Passage of the Bill", "On Cloture on the Motion to Proceed", "On Motion to Suspend the Rules and Pass"));
            Boolean house = false;
            Boolean senate = false;
            for (int i = 0; i < jsonArray.length() ; i++) {
                JSONObject vote = jsonArray.getJSONObject(i);
                if (votingMotions.contains(vote.getString("question"))){
                    String url = vote.getString("api_url");

                    //only get the most recent house and senate vote
                    if (vote.getString("chamber").equals("House") && !house){
                        house = true;
                        getRollCall(url, bill);

                    }
                    else if (vote.getString("chamber").equals("Senate") && !senate){
                        senate = true;
                        getRollCall(url, bill);
                    }
                }
                if (house && senate){
                    break;
                }

            }
        }

        private static void getRollCall(String url, final Bill bill){
            RequestParams params = new RequestParams();
            RequestHeaders headers = new RequestHeaders();
            headers.put("X-API-Key", KEY);
            AsyncHttpClient client = new AsyncHttpClient();
            client.get(url, headers, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.d(TAG, "onSuccess getRollCall");
                    JSONObject jsonObject = json.jsonObject;
                    try{
                        JSONObject votes = jsonObject.getJSONObject("results").getJSONObject("votes").getJSONObject("vote");
                        parseRollCall(votes, bill);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "Hit json exception while parcing, error: " + e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.d(TAG, String.format("onFailure getRollCall: \nstatusCode:%d \nresponse:%s", statusCode, response));
                }
            });

        }

        private static void parseRollCall(JSONObject jsonObject, Bill bill) throws JSONException {
            RollCall rollCall = new RollCall();

            //set roll call for bill
            String chamber = jsonObject.getString("chamber");
            Offices office;
            if (chamber.equals("House")){
                office = Offices.HOUSE_OF_REPRESENTATIVES;
                bill.setHouseRollCall(rollCall);
            }
            else{
                office = Offices.SENATE;
                bill.setSenateRollCall(rollCall);

            }

            //set date
            rollCall.setDate(parseDate(jsonObject.getString("date")));

            //set result
            rollCall.setResult(jsonObject.getString("result"));

            //set voting party breakdowns
            List<String> partyList = new ArrayList<>(Arrays.asList("democratic", "republican", "independent", "total"));
            for (String party: partyList) {
                Map<String, Integer> partyBreakdown = new HashMap<>();
                JSONObject partyJson = jsonObject.getJSONObject(party);
                Iterator<String> keys = partyJson.keys();
                while(keys.hasNext() ) {
                    String typeOfVote = (String)keys.next();
                    if (typeOfVote.equals("majority_position")){
                        continue;
                    }
                    else{
                        int numberOfVotes = partyJson.getInt(typeOfVote);
                        partyBreakdown.put(typeOfVote, numberOfVotes);
                    }
                }
                switch (party){
                    case "democratic":
                        rollCall.setDemocratBreakdown(partyBreakdown);
                        break;
                    case "republican":
                        rollCall.setRepublicanBreakdown(partyBreakdown);
                        break;
                    case "independent":
                        rollCall.setIndependentBreakdown(partyBreakdown);
                        break;
                    case "total":
                        rollCall.setTotalBreakdown(partyBreakdown);
                        break;
                }
            }

            //set voting positions of all representatives
            Map<Representative, String> votingPositions = new HashMap<>();
            rollCall.setVotes(votingPositions);
            JSONArray votes = jsonObject.getJSONArray("positions");
            for (int i = 0; i < votes.length(); i++) {
                JSONObject vote = votes.getJSONObject(i);
                Representative representative = new Representative();
                representative.setName(vote.getString("name"));
                representative.setParty(vote.getString("party"));
                representative.setState(vote.getString("state"));
                representative.setOffice(office);
                rollCall.addVote(representative, vote.getString("vote_position"));
            }

        }

    }

}