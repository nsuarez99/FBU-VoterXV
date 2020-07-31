package com.example.fbu_voterxv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Candidate;

import java.util.List;

public class CandidateListAdapter extends RecyclerView.Adapter<CandidateListAdapter.ViewHolder> {

    private Context context;
    private List<Candidate> candidates;
    private OnClickListener onClickListener;

    public interface OnClickListener{
        void onClick(int position);
    }

    public CandidateListAdapter(Context context, List<Candidate> candidates, OnClickListener onClickListener) {
        this.context = context;
        this.candidates = candidates;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_candidate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Candidate candidate = candidates.get(position);
        holder.bind(candidate);
    }

    @Override
    public int getItemCount() {
        return candidates.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        candidates.clear();
        notifyDataSetChanged();
    }

    // Add a list of candidates
    public void addAll(List<Candidate> list) {
        candidates.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView candidateImage;
        TextView candidateName;
        TextView candidateParty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            candidateImage = itemView.findViewById(R.id.candidateListImage);
            candidateName = itemView.findViewById(R.id.candidateListName);
            candidateParty = itemView.findViewById(R.id.candidateListParty);
        }

        public void bind(Candidate candidate) {
            candidateName.setText(candidate.getName());
            candidateParty.setText(candidate.getParty());
            Glide.with(context).load(candidate.getProfileImage()).placeholder(R.drawable.politician).into(candidateImage);
            setListeners();
        }

        //set on click listeners
        private void setListeners() {
            candidateImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
