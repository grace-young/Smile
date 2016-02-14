package com.treehacks.bestteamever.smile;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static HashMap<String, Double> sentimentMap = new HashMap<String, Double>();

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
            try {
                JSONObject toneJSON = new JSONObject(tone.toString());
                JSONArray toneCategories = toneJSON.getJSONObject("document_tone").getJSONArray("tone_categories");

                JSONArray emotionTones = toneCategories.getJSONObject(0).getJSONArray("tones");

                for (int i = 0; i < emotionTones.length(); i++) {
                    sentimentMap.put(emotionTones.getJSONObject(i).getString("tone_name"), emotionTones.getJSONObject(i).getDouble("score") * 100);
                }

                JSONArray writingTones = toneCategories.getJSONObject(1).getJSONArray("tones");

                for (int i = 0; i < writingTones.length(); i++) {
                    sentimentMap.put(writingTones.getJSONObject(i).getString("tone_name"), emotionTones.getJSONObject(i).getDouble("score") * 100);
                }

                JSONArray socialTones = toneCategories.getJSONObject(2).getJSONArray("tones");

                for (int i = 0; i < socialTones.length(); i++) {
                    sentimentMap.put(socialTones.getJSONObject(i).getString("tone_name"), emotionTones.getJSONObject(i).getDouble("score") * 100);
                }

                Log.d("debug", sentimentMap.toString());

            } catch(Exception JSONException) {
                Log.d("debug", "JSON is not correctly formatted!");
            }

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

