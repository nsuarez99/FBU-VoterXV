package com.example.fbu_voterxv.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fbu_voterxv.R;
import com.example.fbu_voterxv.models.PoliticalView;

import java.util.List;

public class PoliticalViewAdapter extends RecyclerView.Adapter<PoliticalViewAdapter.ViewHolder> {

    private Context context;
    private List<PoliticalView> politicalViews;

    public PoliticalViewAdapter(Context context, List<PoliticalView> politicalViews) {
        this.context = context;
        this.politicalViews = politicalViews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_political_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PoliticalView election = politicalViews.get(position);
        holder.bind(election);
    }

    @Override
    public int getItemCount() {
        return politicalViews.size();
    }

    /**
     * clears entire politicalViews and notifies adapter of change
     */
    public void clear() {
        politicalViews.clear();
        notifyDataSetChanged();
    }

    /**
     * adds list to politicalViews and notifies adapter of change
     * @param list
     */
    public void addAll(List<PoliticalView> list) {
        politicalViews.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView viewSubject;
        TextView viewSummary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewSubject = itemView.findViewById(R.id.viewSubject);
            viewSummary = itemView.findViewById(R.id.viewSummary);
        }

        public void bind(PoliticalView politicalView) {
            viewSubject.setText(politicalView.getSubject());
            viewSummary.setText(politicalView.getSummary());
        }

    }
}
