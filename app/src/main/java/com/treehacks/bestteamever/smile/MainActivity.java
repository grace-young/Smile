package com.treehacks.bestteamever.smile;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

    private Integer happyImages[] = {R.drawable.motivation1, R.drawable.motivation2, R.drawable.motivation3, R.drawable.motivation5,
            R.drawable.motivation6, R.drawable.motivation7, R.drawable.motivation8, R.drawable.motivation9,
            R.drawable.motivation10, R.drawable.motivation11, R.drawable.motivation12, R.drawable.motivation13,
            R.drawable.motivation14, R.drawable.motivation15};
    private int currImage = 0;

    private String mTodayText = "";
    private String mYesterdayText = "";
    private String mPastWeekText = "";

    private boolean today;
    private boolean yesterday;
    private boolean pastWeek;

    private boolean mAnalyzingComplete = false;
    private HashMap<String, Float> mTodaySentimentMap = new HashMap<>();
    private HashMap<String, Float> mYesterdaySentimentMap = new HashMap<>();
    private HashMap<String, Float> mPastWeekSentimentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitialImage();
        setImageRotateListener();
        setCallButtonListener();
        setSurveyButtonListener();

        setAnalysisButtonListener();
        setHistoryButtonListener();

        long startOfDayTime = calculateStartOfDay();
        long startOfYesterdayTime = calculateStartOfYesterday();
        long startOfPastWeekTime = calculateStartOfPastWeekTime();


        String todaySelection = Telephony.Sms.Sent.DATE + " > ?";
        String[] todaySelectionArgs = {String.valueOf(startOfDayTime)};
        List<String> todaySentTexts = getSentTexts(todaySelection, todaySelectionArgs);

        String yesterdaySelection = Telephony.Sms.Sent.DATE + " > ? AND " + Telephony.Sms.Sent.DATE + " < ?";
        String[] yesterdaySelectionArgs = {String.valueOf(startOfYesterdayTime), String.valueOf(startOfDayTime)};
        List<String> yesterdaySentTexts = getSentTexts(yesterdaySelection, yesterdaySelectionArgs);

        String pastWeekSelection = Telephony.Sms.Sent.DATE + " > ?";
        String[] pastWeekSelectionArgs = {String.valueOf(startOfPastWeekTime)};
        List<String> pastWeekSentTexts = getSentTexts(pastWeekSelection, pastWeekSelectionArgs);

        if (todaySentTexts == null || todaySentTexts.size() == 0) {
            today = false;
        } else {
            today = true;
            mTodayText = todaySentTexts.toString();
        }
        if (yesterdaySentTexts == null || yesterdaySentTexts.size() == 0) {
            yesterday = false;
        } else {
            yesterday = true;
            mYesterdayText = yesterdaySentTexts.toString();
        }
        if (pastWeekSentTexts == null || pastWeekSentTexts.size() == 0) {
            pastWeek = false;
        } else {
            pastWeek = true;
            mPastWeekText = pastWeekSentTexts.toString();
        }

//            for (String s : todaySentTexts) {
//                Log.d(TAG, "today: " + s);
//            }
//            for (String s : yesterdaySentTexts) {
//                Log.d(TAG, "yesterday: " + s);
//            }
//            for (String s : pastWeekSentTexts) {
//                Log.d(TAG, "past week: " + s);
//            }

        ToneTask toneTask = new ToneTask();
        toneTask.execute();
    }

    private void setAnalysisButtonListener() {
        Button button = (Button) findViewById(R.id.analysisButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAnalyzingComplete) {
                    Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
                    if (today) {
                        float[] todayYValues = {
                                mTodaySentimentMap.get("Anger"),
                                mTodaySentimentMap.get("Disgust"),
                                mTodaySentimentMap.get("Fear"),
                                mTodaySentimentMap.get("Joy"),
                                mTodaySentimentMap.get("Sadness")
                        };
                        intent.putExtra("todayYValues", todayYValues);
                    }
                    if (yesterday) {
                        float[] yesterdayYValues = {
                                mYesterdaySentimentMap.get("Anger"),
                                mYesterdaySentimentMap.get("Disgust"),
                                mYesterdaySentimentMap.get("Fear"),
                                mYesterdaySentimentMap.get("Joy"),
                                mYesterdaySentimentMap.get("Sadness")
                        };
                        intent.putExtra("yesterdayYValues", yesterdayYValues);
                    }
                    if (pastWeek) {
                        float[] pastWeekYValues = {
                                mPastWeekSentimentMap.get("Anger"),
                                mPastWeekSentimentMap.get("Disgust"),
                                mPastWeekSentimentMap.get("Fear"),
                                mPastWeekSentimentMap.get("Joy"),
                                mPastWeekSentimentMap.get("Sadness")
                        };
                        intent.putExtra("pastWeekYValues", pastWeekYValues);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Analyzing still in progress!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setHistoryButtonListener() {
        Button button = (Button) findViewById(R.id.historyButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
                startActivity(intent);
            }
        });
    }

    private long calculateStartOfDay() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay();
        return dateTime.getMillis();
    }

    private long calculateStartOfYesterday() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay();
        dateTime = dateTime.minusDays(1);
        return dateTime.getMillis();
    }

    private long calculateStartOfPastWeekTime() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.withTimeAtStartOfDay();
        dateTime = dateTime.minusDays(7);
        return dateTime.getMillis();
    }

    private void setImageRotateListener() {
        final ImageButton rotatebutton = (ImageButton) findViewById(R.id.motivation_button);
        rotatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                currImage++;
                if (currImage == 14) {
                    currImage = 0;
                }
                setCurrentImage();
            }
        });
    }

    private void setSurveyButtonListener() {
        final Button surveyButton = (Button) findViewById(R.id.surveyButton);

        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setInitialImage() {
        setCurrentImage();
    }

    private void setCurrentImage() {
        final ImageView imageView = (ImageView) findViewById(R.id.motivation_button);
        imageView.setImageResource(happyImages[currImage]);
    }

    private List<String> getSentTexts(String selection, String[] selectionArgs) {
        List<String> sentTexts = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(Telephony.Sms.Sent.CONTENT_URI,
                new String[]{Telephony.Sms.Sent.BODY},
                selection,
                selectionArgs,
                Telephony.Sms.Sent.DEFAULT_SORT_ORDER);

        if (cursor == null) {
            return sentTexts;
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

            if (today) {
                ToneAnalysis todayTone = service.getTone(mTodayText);
                parseAndStoreResults(todayTone, mTodaySentimentMap);
            }
            if (yesterday) {
                ToneAnalysis yesterdayTone = service.getTone(mYesterdayText);
                parseAndStoreResults(yesterdayTone, mYesterdaySentimentMap);
            }
            if (pastWeek) {
                ToneAnalysis pastWeekTone = service.getTone(mPastWeekText);
                parseAndStoreResults(pastWeekTone, mPastWeekSentimentMap);
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                Log.d(TAG, "Text finished analyzing");
                mAnalyzingComplete = true;
            }
        }
    }

    private void parseAndStoreResults(ToneAnalysis tone, HashMap<String, Float> map) {
        try {
            JSONObject toneJSON = new JSONObject(tone.toString());
            JSONArray toneCategories = toneJSON.getJSONObject("document_tone").getJSONArray("tone_categories");

            JSONArray emotionTones = toneCategories.getJSONObject(0).getJSONArray("tones");

            for (int i = 0; i < emotionTones.length(); i++) {
                map.put(emotionTones.getJSONObject(i).getString("tone_name"), (float) (emotionTones.getJSONObject(i).getDouble("score") * 100));
            }

            JSONArray writingTones = toneCategories.getJSONObject(1).getJSONArray("tones");

            for (int i = 0; i < writingTones.length(); i++) {
                map.put(writingTones.getJSONObject(i).getString("tone_name"), (float) (emotionTones.getJSONObject(i).getDouble("score") * 100));
            }

            JSONArray socialTones = toneCategories.getJSONObject(2).getJSONArray("tones");

            for (int i = 0; i < socialTones.length(); i++) {
                map.put(socialTones.getJSONObject(i).getString("tone_name"), (float) (emotionTones.getJSONObject(i).getDouble("score") * 100));
            }

            Log.d(TAG, map.toString());

        } catch (Exception JSONException) {
            Log.e(TAG, "JSON is not correctly formatted!");
        }
    }

    private void setCallButtonListener() {
        final Button callButton = (Button) findViewById(R.id.call_button);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d(TAG, "call button clicked!");
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    String teleString = getString(R.string.telephoneParseStr);
                    callIntent.setData(Uri.parse(teleString));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e(TAG, "Call failed", e);
                }
            }
        });
    }

    public void sootheClick(View view) {
        try {
            Intent audioIntent = new Intent(this, Soothe.class);
            //String teleString = getString(R.string.telephoneParseStr);
            //audioIntent.setData(Uri.parse(teleString));
            startActivity(audioIntent);
        } catch (ActivityNotFoundException e) {
            Log.e("problem", "Call failed", e);
        }
    }
}

