package com.example.fbu_voterxv.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Bill;
import com.example.fbu_voterxv.models.Offices;
import com.example.fbu_voterxv.models.Representative;
import com.example.fbu_voterxv.models.RollCall;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class VotingHistoryAdapter extends RecyclerView.Adapter<VotingHistoryAdapter.ViewHolder> {

    public static final String TAG = "VotingHistoryFragment";
    private Context context;
    private List<Bill> bills;
    private Representative representative;
    private Map<Bill, String> votes;

    public VotingHistoryAdapter(Context context, List<Bill> bills, Representative representative, Map<Bill, String> votes) {
        this.context = context;
        this.bills = bills;
        this.representative = representative;
        this.votes = votes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bill bill = bills.get(position);
        holder.bind(bill);
    }

    @Override
    public int getItemCount() {
        return bills.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        bills.clear();
        notifyDataSetChanged();
    }

    // Add a list of bills
    public void addAll(List<Bill> list) {
        bills.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView votingCheckbox;
        TextView billTitle;
        TextView billDate;
        TextView billSummary;
        TextView billVotingRecord;
        RelativeLayout billLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            votingCheckbox = itemView.findViewById(R.id.billCheckbox);
            billTitle = itemView.findViewById(R.id.billTitle);
            billDate = itemView.findViewById(R.id.billDate);
            billSummary = itemView.findViewById(R.id.billSummary);
            billVotingRecord = itemView.findViewById(R.id.billVotingRecord);
            billLayout = itemView.findViewById(R.id.bill);
        }

        public void bind(Bill bill) {
            //get vote of representative and if not in congress then skips bill
            String vote = votes.get(bill);
            if (vote.equals("Yes")){
                Glide.with(context).load(R.drawable.checkmark).into(votingCheckbox);
            }
            else if (vote.equals("No")){
                Glide.with(context).load(android.R.drawable.ic_delete).into(votingCheckbox);
            }
            else{
                Glide.with(context).load(R.drawable.absent).into(votingCheckbox);
            }

            Log.i(TAG, bill.getCosponsors().toString());
            Log.i(TAG, bill.getSponsor().getParty());

            billSummary.setText(bill.getBriefSummary());
            billTitle.setText(bill.getTitle());


            if (representative.getOffice() == Offices.PRESIDENT || representative.getOffice() == Offices.VICE_PRESIDENT){
                //set date
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                String date = "";
                if (bill.getVeto() != null){
                    date = formatter.format(bill.getVeto());

                }
                else{
                    date = formatter.format(bill.getLaw());
                }
                billDate.setText(date);

                //set vote breakdown
                RollCall rollCallSenate = bill.getSenateRollCall();
                RollCall rollCallHouse = bill.getHouseRollCall();

                int senateYes;
                int senateNo;
                int houseYes;
                int houseNo;

                //if the vote was a voice vote and not roll call then make unanimous
                if (rollCallSenate == null){
                    senateYes = 100;
                    senateNo = 0;
                }
                else{
                    senateYes = rollCallSenate.getTotalBreakdown().get("yes");
                    senateNo = rollCallSenate.getTotalBreakdown().get("no");
                }
                if (rollCallHouse == null){
                    houseYes = 435;
                    houseNo = 400;
                }
                else{
                    houseYes = rollCallHouse.getTotalBreakdown().get("yes");
                    houseNo = rollCallHouse.getTotalBreakdown().get("no");
                }

                //set the total vote record
                billVotingRecord.setText(String.format("Senate: %d - %d House: %d - %d",senateYes , senateNo, houseYes, houseNo));

            }
            else{
                //get roll call of representative
                RollCall rollCall;
                if (representative.getOffice() == Offices.SENATE){
                    rollCall = bill.getSenateRollCall();
                }
                else{
                    rollCall = bill.getHouseRollCall();
                }

                //set date
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                String date = formatter.format(rollCall.getDate());
                billDate.setText(date);

                //set the total vote record
                billVotingRecord.setText(String.format("%d - %d (D: %d, R: %d, I:%d) %s", rollCall.getTotalBreakdown().get("yes"), rollCall.getTotalBreakdown().get("no"),
                        rollCall.getDemocratBreakdown().get("yes"), rollCall.getRepublicanBreakdown().get("yes"), rollCall.getIndependentBreakdown().get("yes"), rollCall.getResult()));
            }
        }

    }

}
