package com.treehacks.bestteamever.smile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Integer happyImages[] = {R.drawable.motivation1, R.drawable.motivation2, R.drawable.motivation3, R.drawable.motivation5,
                                        R.drawable.motivation6, R.drawable.motivation7, R.drawable.motivation8, R.drawable.motivation9,
                                    R.drawable.motivation10, R.drawable.motivation11, R.drawable.motivation12, R.drawable.motivation13,
                                    R.drawable.motivation14};
    private int currImage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialImage();
        setImageRotateListener();
        setSurveyButtonListener();
    }


    private void setImageRotateListener() {
        final ImageButton rotatebutton = (ImageButton) findViewById(R.id.motivation_button);
        rotatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                currImage++;
                if (currImage == 12) {
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
}
