package com.treehacks.bestteamever.smile;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

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
            Log.d(TAG, sentTexts.toString());
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
}
