package com.treehacks.bestteamever.smile;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ResultsActivity extends ListActivity {

    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        items = new ArrayList<>();
        items.add("Monday");
        items.add("Tuesday");
        items.add("Wednesday");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        items.remove(position);
        adapter.notifyDataSetChanged();
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
            setListAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}
