package com.treehacks.bestteamever.smile;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    List<String> lstSms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getAllSmsFromProvider();
        Log.d(TAG, lstSms.get(0));
    }

    public List<String> getAllSmsFromProvider() {
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Sent.CONTENT_URI, // Official CONTENT_URI from docs
                new String[] { Telephony.Sms.Sent.BODY }, // Select body text
                null,
                null,
                Telephony.Sms.Sent.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                lstSms.add(c.getString(0));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        return lstSms;
    }
}
