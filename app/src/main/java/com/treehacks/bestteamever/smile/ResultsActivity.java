package com.treehacks.bestteamever.smile;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = ResultsActivity.class.getSimpleName();

    private ListView listView;

    private ArrayList<String> items;
    private ArrayList<String> emotionsList;

    private ArrayList<String> adapterList;
    private ArrayAdapter<String> adapter;

    private boolean datesShown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        listView = (ListView) findViewById(R.id.listView);

        items = new ArrayList<>();
        emotionsList = new ArrayList<>();
        getSurveyResults();


        adapterList = new ArrayList<>(items);

        adapter = new ArrayAdapter<>(this, R.layout.results_list_item, adapterList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void getSurveyResults() {
        for (long time : Global.globalSurveyMap.keySet()) {
            LocalDate localDate = new LocalDate(time);
            String date = localDate.dayOfWeek().getAsText() + ", " + localDate.monthOfYear().getAsText() + " " + localDate.dayOfMonth().getAsText();
            items.add(date);

            String emotions = "";
            for (String s : Global.globalSurveyMap.get(time)) {
                emotions += s + " | ";
            }
            if (emotions.length() >= 3) {
                emotions = emotions.substring(0, emotions.length() - 3);
            }

            emotionsList.add(emotions);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toggleDateAndEmotions(view, position);
        adapter.notifyDataSetChanged();
    }

    private void toggleDateAndEmotions(View v, int pos) {

        if (datesShown) {
            // show emotions
            setEmotionFormatting(v);
            adapterList.set(pos, emotionsList.get(pos));
            datesShown = false;
        } else {
            // show dates
            setDateFormatting(v);
            adapterList.set(pos, items.get(pos));
            datesShown = true;
        }
    }

    private void setEmotionFormatting(View v) {
        v.setBackgroundColor(Color.parseColor("#F57F17"));
        TextView textView = (TextView) v.findViewById(R.id.itemText);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextSize(16);
    }

    private void setDateFormatting(View v) {
        v.setBackgroundColor(Color.parseColor("#FFF9C4"));
        TextView textView = (TextView) v.findViewById(R.id.itemText);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTypeface(Typeface.DEFAULT);
        textView.setTextSize(20);
    }
}
