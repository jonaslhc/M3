package com.interaxon.test.libmuse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
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
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stroop_result_layout);
        menuActivity = new MenuActivity();
        initView();
        initBarGraph();
    }

    private void initBarGraph() {

        mChart.setNoDataText("No data is currently available");
        mChart.setDescription("");
        mChart.setDrawHighlightArrow(true);

        Log.e(TAG, "current user name: " + DatabaseHandler.getHandler().getCurrUser().getName());
        Typeface tf = Typeface.DEFAULT;
        profileData = databaseHandler.getHandler().getData(DatabaseHandler.getHandler().getCurrUser().getName());

        last_accuracy = profileData.getAccuracy();
        last_reaction_time = profileData.getReaction_time();

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

        ArrayList<BarEntry> accuracy = new ArrayList<>();
        ArrayList<BarEntry> reaction_time = new ArrayList<>();


        // Populate with values
        accuracy.add(new BarEntry((float) profileData.getAccuracy(), 0));
        reaction_time.add(new BarEntry((float) profileData.getReaction_time(), 1));

        for(int i = 1; i < 6; i ++){
            accuracy.add(new BarEntry((float) i/10, i));
            reaction_time.add(new BarEntry((float) i/10, i));
        }

        BarDataSet accuracy_set = new BarDataSet(accuracy, "Accuracy");
        BarDataSet reaction_score = new BarDataSet(reaction_time, "Reaction Score");

        accuracy_set.setAxisDependency(YAxis.AxisDependency.LEFT);
        accuracy_set.setColor(ColorTemplate.getHoloBlue());
        //accuracy_set.setHighLightColor(Color.rgb(244, 117, 117));
        accuracy_set.setValueTextColor(Color.WHITE);
        accuracy_set.setValueTextSize(10f);
        accuracy_set.setDrawValues(false);

        reaction_score.setAxisDependency(YAxis.AxisDependency.LEFT);
        reaction_score.setColor(Color.BLACK);
        //reaction_score.setHighLightColor(Color.rgb(244, 117, 117));
        reaction_score.setValueTextColor(Color.WHITE);
        reaction_score.setValueTextSize(10f);
        reaction_score.setDrawValues(false);


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(accuracy_set);
        dataSets.add(reaction_score);


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(profileData.getName());
        xVals.add("Jin Hee");
        xVals.add("Andrea");
        xVals.add("Jia");
        xVals.add("Jonas");
        xVals.add("Orca Friend");
        xVals.add("HeartBear");

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.BLACK);

        BarData mData = new BarData(xVals, dataSets);
        //mData.setValueFormatter(new PercentFormatter());
        mData.setValueTextSize(10f);
        mData.setValueTextColor(Color.GRAY);
        mChart.setDrawValueAboveBar(true);
        mChart.setData(mData);
        mChart.setDrawHighlightArrow(true);
        mChart.invalidate();
    }



    private void initView() {
        mChart = (BarChart) findViewById(R.id.bar_chart);
        backButton = (Button) findViewById(R.id.back_to_menu);

    }


    public void Back(View view) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
