package com.treehacks.bestteamever.smile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class GraphActivity extends AppCompatActivity {

    private RadarChart mChart;

    private boolean today = true;
    private boolean yesterday = true;
    private boolean pastWeek = true;
    private boolean details = false;

    private float[] todayYVals;
    private float[] yesterdayYVals;
    private float[] pastWeekYVals;

    private String[] mEmotions = new String[]{
            "Anger", "Disgust", "Fear", "Joy", "Sadness"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Intent intent = getIntent();
        todayYVals = intent.getFloatArrayExtra("todayYValues");
        yesterdayYVals = intent.getFloatArrayExtra("yesterdayYValues");
        pastWeekYVals = intent.getFloatArrayExtra("pastWeekYValues");

        if (todayYVals == null) {
            today = false;
        }
        if (yesterdayYVals == null) {
            yesterday = false;
        }
        if (pastWeekYVals == null) {
            pastWeek = false;
        }


        Button shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!today && !yesterday && !pastWeek) {
                    Toast.makeText(getApplicationContext(), "No data to send!", Toast.LENGTH_LONG);
                    return;
                }
                String data = "Emotions: " + Arrays.toString(mEmotions) + "\n";
                if (today) {
                    data += "Today's data: " + Arrays.toString(todayYVals) + "\n";
                }
                if (yesterday) {
                    data += "Yesterday's data: " + Arrays.toString(yesterdayYVals) + "\n";
                }
                if (pastWeek) {
                    data += "Past Week's data: " + Arrays.toString(pastWeekYVals) + "\n";
                }

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_SUBJECT, "Text Analysis Data");
                i.putExtra(Intent.EXTRA_TEXT, data);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setCheckBoxListeners();

        mChart = (RadarChart) findViewById(R.id.chart);

        setData();

        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(14f);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(14f);
        yAxis.setAxisMinValue(0f);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        l.setTextSize(16f);
        l.setYEntrySpace(5f);

        mChart.setDescription(null);
    }

    private void setCheckBoxListeners() {
        CheckBox todayCheckbox = (CheckBox) findViewById(R.id.todayCheckbox);
        CheckBox yesterdayCheckbox = (CheckBox) findViewById(R.id.yesterdayCheckbox);
        CheckBox pastWeekCheckbox = (CheckBox) findViewById(R.id.pastWeekCheckbox);
        CheckBox detailsCheckbox = (CheckBox) findViewById(R.id.detailsCheckbox);

        if (!today) {
            todayCheckbox.setEnabled(false);
        }
        if (!yesterday) {
            yesterdayCheckbox.setEnabled(false);
        }
        if (!pastWeek) {
            pastWeekCheckbox.setEnabled(false);
        }

        todayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    today = true;
                    setData();
                } else {
                    today = false;
                    setData();
                }
            }
        });

        yesterdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    yesterday = true;
                    setData();
                } else {
                    yesterday = false;
                    setData();
                }
            }
        });

        pastWeekCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pastWeek = true;
                    setData();
                } else {
                    pastWeek = false;
                    setData();
                }
            }
        });

        detailsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    details = true;
                    setData();
                } else {
                    details = false;
                    setData();
                }
            }
        });
    }

    public void setData() {

        if (!today && !yesterday && !pastWeek) {
            mChart.clear();
            return;
        }

        int cnt = mEmotions.length;

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();
        ArrayList<Entry> yVals3 = new ArrayList<>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.

        if (today) {
            for (int i = 0; i < cnt; i++) {
                yVals1.add(new Entry(todayYVals[i], i));
            }
        }

        if (yesterday) {
            for (int i = 0; i < cnt; i++) {
                yVals2.add(new Entry(yesterdayYVals[i], i));
            }
        }

        if (pastWeek) {
            for (int i = 0; i < cnt; i++) {
                yVals3.add(new Entry(pastWeekYVals[i], i));
            }
        }

        ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mEmotions[i % mEmotions.length]);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        RadarDataSet set1, set2, set3;

        if (today) {
            set1 = new RadarDataSet(yVals1, "Today");
            set1.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
            set1.setFillColor(ColorTemplate.VORDIPLOM_COLORS[4]);
            set1.setDrawFilled(true);
            set1.setLineWidth(2f);
            sets.add(set1);
        }

        if (yesterday) {
            set2 = new RadarDataSet(yVals2, "Yesterday");
            set2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            set2.setFillColor(ColorTemplate.VORDIPLOM_COLORS[0]);
            set2.setDrawFilled(true);
            set2.setLineWidth(2f);
            sets.add(set2);
        }

        if (pastWeek) {
            set3 = new RadarDataSet(yVals3, "Past Week");
            set3.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);
            set3.setFillColor(ColorTemplate.VORDIPLOM_COLORS[2]);
            set3.setDrawFilled(true);
            set3.setLineWidth(2f);
            sets.add(set3);
        }

        RadarData data = new RadarData(xVals, sets);
        data.setValueTextSize(12f);
        if (!details) {
            data.setDrawValues(false);
        }

        mChart.setData(data);
        mChart.invalidate();
    }
}
