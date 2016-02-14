package com.treehacks.bestteamever.smile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class SurveyRecyclerViewAdapter extends RecyclerView.Adapter<SurveyViewHolders> {

    private List<ItemObjects> itemList;
    private Context context;

    public SurveyRecyclerViewAdapter(Context context, List<ItemObjects> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SurveyViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.survey_list, null);
        SurveyViewHolders rcv = new SurveyViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SurveyViewHolders holder, int position) {
        holder.countryName.setText(itemList.get(position).getName());
        holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
