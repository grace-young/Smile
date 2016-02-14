package com.treehacks.bestteamever.smile;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private String mText = "";
    private HashMap<String, Integer> sentimentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> sentTexts = getSentTextsFromCurrentDay();

        if (sentTexts == null) {
            Log.e(TAG, "No texts sent in last day!");
        } else {
//            for (String s : sentTexts) {
//                Log.d(TAG, s);
//            }

            mText = sentTexts.toString();
            ToneTask toneTask = new ToneTask();
            toneTask.execute();
        }

    }

    public List<String> getSentTextsFromCurrentDay() {
        List<String> sentTexts = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();

        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay();
        long startOfDayTime = dateTime.getMillis();

        String selection = Telephony.Sms.Sent.DATE + " > ?";
        String[] selectionArgs = { String.valueOf(startOfDayTime) };

        Cursor cursor = contentResolver.query(Telephony.Sms.Sent.CONTENT_URI,
                new String[]{Telephony.Sms.Sent.BODY},
                selection,
                selectionArgs,
                Telephony.Sms.Sent.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return null;
        }

        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                sentTexts.add(cursor.getString(cursor.getColumnIndex(Telephony.Sms.Sent.BODY)));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return sentTexts;

    }

    private class ToneTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            ToneAnalyzer service = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_02_11);
            service.setUsernameAndPassword("dbd0722f-64ce-4d9e-904c-01ae273be401", "q4nNXEbBFQ8B");

            ToneAnalysis tone = service.getTone(mText);
            try {
                JSONObject toneJSON = new JSONObject(tone.toString());
                JSONArray toneCategories = toneJSON.getJSONObject("document_tone").getJSONArray("tone_categories");

                JSONArray emotionTones = toneCategories.getJSONObject(0).getJSONArray("tones");

                for (int i = 0; i < emotionTones.length(); i++) {
                    sentimentMap.put(emotionTones.getJSONObject(i).getString("tone_name"), (int) emotionTones.getJSONObject(i).getDouble("score") * 100);
                }

                JSONArray writingTones = toneCategories.getJSONObject(1).getJSONArray("tones");

                for (int i = 0; i < writingTones.length(); i++) {
                    sentimentMap.put(writingTones.getJSONObject(i).getString("tone_name"), (int) emotionTones.getJSONObject(i).getDouble("score") * 100);
                }

                JSONArray socialTones = toneCategories.getJSONObject(2).getJSONArray("tones");

                for (int i = 0; i < socialTones.length(); i++) {
                    sentimentMap.put(socialTones.getJSONObject(i).getString("tone_name"), (int) emotionTones.getJSONObject(i).getDouble("score") * 100);
                }

                Log.d(TAG, sentimentMap.toString());

            } catch (Exception JSONException) {
                Log.d(TAG, "JSON is not correctly formatted!");
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Log.d(TAG, "Text finished analyzing");
            }
        }
    }
}

