package com.treehacks.bestteamever.smile;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToneTask toneTask = new ToneTask();
        toneTask.execute();
    }

    private class ToneTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_02_11);
            service.setUsernameAndPassword("dbd0722f-64ce-4d9e-904c-01ae273be401", "q4nNXEbBFQ8B");

            String text = "I know the times are difficult! Our sales have been "
                    + "disappointing for the past three quarters for our data analytics "
                    + "product suite. We have a competitive data analytics product "
                    + "suite in the industry. But we need to do our job selling it! "
                    + "We need to acknowledge and fix our sales challenges.";

            ToneAnalysis tone = service.getTone(text);
            Log.d("debug", tone.toString());

            return true;
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Log.d("debug", "Congrats, Catherina!");
            }
        }
    }
}

