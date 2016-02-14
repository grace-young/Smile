package com.treehacks.bestteamever.smile;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SurveyViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final SparseArray<String> surveyMap = new SparseArray<String>(){
        {
            put(0, "Calm");
            put(1, "Happy");
            put(2, "Balanced");
            put(3, "Angry");
            put(4, "Focused");
            put(5, "Lighthearted");
            put(6, "Brooding");
            put(7, "Stressed");
            put(8, "Excited");
            put(9, "Bored");
            put(10, "Playful");
            put(11, "Sad");
            put(12, "Burnt Out");
            put(13, "Balanced");
            put(14, "Depressed");
        }
    };

    public TextView countryName;
    public ImageView countryPhoto;

    public SurveyViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {
        Global.surveySet.add((surveyMap.get(getLayoutPosition())));

        CardView cv = (CardView) view;
        RelativeLayout rl = (RelativeLayout) cv.getChildAt(0);
        TextView tm = (TextView) rl.findViewById(R.id.country_name);
        tm.setBackgroundColor(Color.parseColor("#F57F17"));
    }
}
