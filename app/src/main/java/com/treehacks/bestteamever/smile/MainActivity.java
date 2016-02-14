package com.treehacks.bestteamever.smile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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


    private void setInitialImage() {
        setCurrentImage();
    }

    private void setCurrentImage() {

        final ImageView imageView = (ImageView) findViewById(R.id.motivation_button);
        imageView.setImageResource(happyImages[currImage]);

    }


}
