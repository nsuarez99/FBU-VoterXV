package com.example.fbu_voterxv.adapters;

import android.content.Context;
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

    private Context context;
    private List<Bill> bills;
    private OnClickListener onClickListener;
    private Representative representative;

    public interface OnClickListener{
        void onClick(int position);
    }

    public VotingHistoryAdapter(Context context, List<Bill> bills, Representative representative) {
        this.context = context;
        this.bills = bills;
        this.representative = representative;
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
            //get roll call of representative
            RollCall rollCall;
            if (representative.getOffice() == Offices.SENATE){
                rollCall = bill.getSenateRollCall();
            }
            else if (representative.getOffice() == Offices.HOUSE_OF_REPRESENTATIVES) {
                rollCall = bill.getHouseRollCall();
            }
            else{
                billLayout.setVisibility(View.GONE);
                return;
            }

            //if there is no vote in that chamber for that bill
            if (rollCall == null){
                billLayout.setVisibility(View.GONE);
                return;
            }

            //get vote of representative and if not in congress then skips bill
            String vote = getRepresentativeVote(rollCall);
            if (vote.equals("Yes")){
                Glide.with(context).load(R.drawable.checkmark).into(votingCheckbox);
            }
            else if (vote.equals("No")){
                Glide.with(context).load(android.R.drawable.ic_delete).into(votingCheckbox);
            }
            else{
                Glide.with(context).load(R.drawable.absent).into(votingCheckbox);
            }

            //set the total vote record
            billVotingRecord.setText(String.format("%d - %d (D: %d, R: %d, I:%d)", rollCall.getTotalBreakdown().get("yes"), rollCall.getTotalBreakdown().get("no"),
                    rollCall.getDemocratBreakdown().get("yes"), rollCall.getRepublicanBreakdown().get("yes"), rollCall.getIndependentBreakdown().get("yes")));


            billSummary.setText(bill.getBriefSummary());
            billTitle.setText(bill.getTitle());

            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String date = formatter.format(rollCall.getDate());
            billDate.setText(date);

//            setListeners();
        }

        private String getRepresentativeVote(RollCall rollCall){
            Map<Representative, String> votes = rollCall.getVotes();
            for (Representative rep : votes.keySet()) {
                if (rep.equals(representative)){
                    return votes.get(rep);
                }
            }
            return "N/A";
        }

//        //set on click listeners
//        private void setListeners() {
//            votingCheckbox.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickListener.onClick(getAdapterPosition());
//                }
//            });
//        }
    }
}
