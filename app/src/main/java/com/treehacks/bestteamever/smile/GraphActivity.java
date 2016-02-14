package com.treehacks.bestteamever.smile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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

public class GraphActivity extends AppCompatActivity {

    private RadarChart mChart;

    private String[] mEmotions = new String[]{
            "Anger", "Disgust", "Fear", "Joy", "Sadness"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Intent intent = getIntent();
        int[] yVals = intent.getIntArrayExtra("yValues");

        mChart = (RadarChart) findViewById(R.id.chart);

        setData(yVals);

        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart.getXAxis();
//        xAxis.setTypeface(tf);
        xAxis.setTextSize(14f);

        YAxis yAxis = mChart.getYAxis();
//        yAxis.setTypeface(tf);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(14f);
        yAxis.setAxisMinValue(0f);

        Legend l = mChart.getLegend();
        l.setCustom(new int[] { ColorTemplate.VORDIPLOM_COLORS[4], ColorTemplate.VORDIPLOM_COLORS[0] }, new String[] { "Yesterday", "Today" });
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
//        l.setTypeface(tf);
//        l.setXEntrySpace(20f);
        l.setTextSize(16f);
        l.setYEntrySpace(5f);

        mChart.setDescription(null);
    }

    public void setData(int[] y2) {

        float mult = 50;
        int cnt = mEmotions.length;

        ArrayList<Entry> yVals1 = new ArrayList<>();
        ArrayList<Entry> yVals2 = new ArrayList<>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }
        
        for (int i = 0; i < y2.length; i++) {
            yVals2.add(new Entry((float) y2[i], i));
        }

//        for (int i = 0; i < cnt; i++) {
//            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
//        }

        ArrayList<String> xVals = new ArrayList<>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mEmotions[i % mEmotions.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        set1.setFillColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "Set 2");
        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set2.setFillColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(xVals, sets);
//        data.setValueTypeface(tf);
//        data.setValueTextSize(12f);
        data.setDrawValues(false);

        mChart.setData(data);

        mChart.invalidate();
    }
}
