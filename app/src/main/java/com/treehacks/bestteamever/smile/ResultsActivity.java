package com.treehacks.bestteamever.smile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = ResultsActivity.class.getSimpleName();

    private ListView listView;

    private ArrayList<String> items;
    private ArrayList<String> adapterList;
    private ArrayAdapter<String> adapter;

    private boolean datesShown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        listView = (ListView) findViewById(R.id.listView);

        items = new ArrayList<>();
        items.add("Monday");
        items.add("Tuesday");
        items.add("Wednesday");

        adapterList = new ArrayList<>(items);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, adapterList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toggleDateAndEmotions(position);

        adapter.notifyDataSetChanged();
    }

    private void toggleDateAndEmotions(int pos) {
        if (datesShown) {
            // show emotions
            adapterList.set(pos, "hi");
            datesShown = false;
        } else {
            // show dates
            adapterList.set(pos, items.get(pos));
            datesShown = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        bundle.putStringArrayList("items", items);
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);

        items = bundle.getStringArrayList("items");

        if (items != null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}
