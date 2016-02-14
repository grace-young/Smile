package com.treehacks.bestteamever.smile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class SurveyActivity extends AppCompatActivity {

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey);

        Global.surveySet = new HashSet<>();

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.globalSurveyMap.put(System.currentTimeMillis(), Global.surveySet);
                Log.d("debug", Global.globalSurveyMap.toString());
                finish();
            }
        });

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        List<ItemObjects> gaggeredList = getListItemData();

        SurveyRecyclerViewAdapter rcAdapter = new SurveyRecyclerViewAdapter(SurveyActivity.this, gaggeredList);
        recyclerView.setAdapter(rcAdapter);
    }

    private List<ItemObjects> getListItemData(){
        List<ItemObjects> listViewItems = new ArrayList<ItemObjects>();
        listViewItems.add(new ItemObjects("Calm", R.drawable.teal));
        listViewItems.add(new ItemObjects("Happy", R.drawable.lightorange));
        listViewItems.add(new ItemObjects("Balanced", R.drawable.deepgreen));
        listViewItems.add(new ItemObjects("Angry", R.drawable.red));
        listViewItems.add(new ItemObjects("Focused", R.drawable.purple));
        listViewItems.add(new ItemObjects("Lighthearted", R.drawable.palepink));
        listViewItems.add(new ItemObjects("Brooding", R.drawable.maroon));
        listViewItems.add(new ItemObjects("Stressed", R.drawable.lightgreen));
        listViewItems.add(new ItemObjects("Excited", R.drawable.pink));
        listViewItems.add(new ItemObjects("Bored", R.drawable.white));
        listViewItems.add(new ItemObjects("Playful", R.drawable.lightblue));
        listViewItems.add(new ItemObjects("Sad", R.drawable.deepblue));
        listViewItems.add(new ItemObjects("Burnt Out", R.drawable.burntorange));
        listViewItems.add(new ItemObjects("Balanced", R.drawable.brown));
        listViewItems.add(new ItemObjects("Depressed", R.drawable.black));

        return listViewItems;
    }
}

