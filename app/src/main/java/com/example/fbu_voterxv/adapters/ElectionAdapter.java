package com.example.fbu_voterxv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.Election;
import com.example.fbu_voterxv.models.Offices;
import com.parse.ParseFile;

import java.util.List;

public class ElectionAdapter extends RecyclerView.Adapter<ElectionAdapter.ViewHolder> {

    private Context context;
    private List<Election> elections;
    private OnClickListener onClickListener;

    public interface OnClickListener{
        void onClick(int position);
    }

    public ElectionAdapter(Context context, List<Election> elections, OnClickListener onClickListener) {
        this.context = context;
        this.elections = elections;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_election, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Election election = elections.get(position);
        holder.bind(election);
    }

    @Override
    public int getItemCount() {
        return elections.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        elections.clear();
        notifyDataSetChanged();
    }

    // Add a list of elections
    public void addAll(List<Election> list) {
        elections.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        Button electionButton;
        TextView electionDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            electionButton = itemView.findViewById(R.id.electionButton);
            electionDate = itemView.findViewById(R.id.electionDate);
        }

        public void bind(Election election) {
            electionButton.setText(election.getName() + " -\n" + election.getOffice());
            electionDate.setText(election.getDateString());
            setListeners();
        }

        private void setListeners() {
            electionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });

            electionDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
