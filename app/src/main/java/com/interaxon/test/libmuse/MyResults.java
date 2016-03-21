package com.interaxon.test.libmuse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by st924507 on 2016-03-14.
 */
public class MyResults extends Activity {

    double last_accuracy, last_reaction_time;
    static ProfileData profileData;
    TextView lastAccuracy, lastReactionTime;
    DatabaseHandler databaseHandler;
    String TAG = MyResults.class.getSimpleName();
    MenuActivity menuActivity;
    BarChart mChart;
    BarData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stroop_result_layout);
        menuActivity = new MenuActivity();
        initView();
        initBarGraph();
    }

    private void initBarGraph() {
        mChart = new BarChart(this);
        mChart.setDescription("Comparison of your results");
        mChart.setNoDataText("No data is currently available");

        Log.e(TAG, "current user name: " + DatabaseHandler.getHandler().getCurrUser().getName());
        Typeface tf = Typeface.DEFAULT;
        profileData = databaseHandler.getHandler().getData(DatabaseHandler.getHandler().getCurrUser().getName());

        last_accuracy = profileData.getAccuracy();
        last_reaction_time = profileData.getReaction_time();

        lastAccuracy.setText(String.format("Accuracy: %6.2f", last_accuracy));
        lastReactionTime.setText(String.format("Reaction Time: %6.2f", last_reaction_time));


        XAxis horizontal_axis = mChart.getXAxis();
        YAxis vertical_axis = mChart.getAxis(YAxis.AxisDependency.LEFT);

        // x-axis
        horizontal_axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        horizontal_axis.setTextSize(10f);
        horizontal_axis.setDrawAxisLine(true);
        horizontal_axis.setDrawGridLines(true);

        // y-axis
        vertical_axis.setDrawLabels(true);
        vertical_axis.setDrawAxisLine(true);
        vertical_axis.setDrawGridLines(true);
        vertical_axis.setDrawZeroLine(true);

        List<BarEntry> accuracy = new ArrayList<BarEntry>();
        List<BarEntry> reaction_time = new ArrayList<BarEntry>();

        // Populate with values
        accuracy.add(new BarEntry((float) profileData.getAccuracy(), 0));
        reaction_time.add(new BarEntry((float) profileData.getReaction_time(), 0));

        BarDataSet accuracy_set = new BarDataSet(accuracy, "Accuracy");
        BarDataSet reaction_score = new BarDataSet(reaction_time, "Reaction Score");

        accuracy_set.setAxisDependency(YAxis.AxisDependency.LEFT);
        accuracy_set.setColor(ColorTemplate.getHoloBlue());
        accuracy_set.setHighLightColor(Color.rgb(244, 117, 117));
        accuracy_set.setValueTextColor(Color.WHITE);
        accuracy_set.setValueTextSize(9f);
        accuracy_set.setDrawValues(false);

        reaction_score.setAxisDependency(YAxis.AxisDependency.LEFT);
        reaction_score.setColor(Color.BLACK);
        reaction_score.setHighLightColor(Color.rgb(244, 117, 117));
        reaction_score.setValueTextColor(Color.WHITE);
        reaction_score.setValueTextSize(9f);
        reaction_score.setDrawValues(false);


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(accuracy_set);
        dataSets.add(reaction_score);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(profileData.getName());
        xVals.add(profileData.getUsername());

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.WHITE);


        mData = new BarData(xVals, dataSets);
        mData.setValueFormatter(new PercentFormatter());
        mData.setValueTextSize(10f);
        mData.setValueTextColor(Color.GRAY);
        mChart.setData(mData);
        mChart.invalidate();
    }



    private void initView() {
        lastAccuracy = (TextView) findViewById(R.id.last_accuracy);
        lastReactionTime = (TextView) findViewById(R.id.last_reaction_time);
        mChart = (BarChart) findViewById(R.id.bar_chart);
    }



}
