package com.treehacks.bestteamever.smile;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Integer happyImages[] = {R.drawable.motivation1, R.drawable.motivation2, R.drawable.motivation3, R.drawable.motivation5,
            R.drawable.motivation6, R.drawable.motivation7, R.drawable.motivation8, R.drawable.motivation9,
            R.drawable.motivation10, R.drawable.motivation11, R.drawable.motivation12, R.drawable.motivation13,
            R.drawable.motivation14, R.drawable.motivation15};
    private int currImage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialImage();
        setImageRotateListener();
        setCallButtonListener();
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


    private void setInitialImage() {
        setCurrentImage();
    }

    private void setCurrentImage() {

        final ImageView imageView = (ImageView) findViewById(R.id.motivation_button);
        imageView.setImageResource(happyImages[currImage]);

    }


    private void setCallButtonListener() {
        final Button callButton = (Button) findViewById(R.id.call_button);
        Log.e("message", "call button clicked!");
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    String teleString = getString(R.string.telephoneParseStr);
                    callIntent.setData(Uri.parse(teleString));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("problem", "Call failed", e);
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
