package com.interaxon.test.libmuse.StroopInfo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by st924507 on 2016-03-23.
 */
public class StroopPeersScore extends Fragment {

    double last_accuracy, last_reaction_time;
    PieChart mChartReaction, mChartAccuracy;
    String TAG = StroopPeersScore.class.getSimpleName();
    static ProfileData profileData;
    DatabaseHandler databaseHandler;
    private String[] xData = {"Your data", "Your Peers' Data"};
    private double dummy_accuracy_mean = 0.8, dummy_reaction_mean = 0.5;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_stroop_peers_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileData = databaseHandler.getHandler().getData(DatabaseHandler.getHandler().getCurrUser().getName());
        initAccuracy();
        initReaction();
    }

    private void initReaction() {
        final int percentile_reaction = findReactionPercentile(profileData.getReaction_time());

        mChartReaction = (PieChart) getActivity().findViewById(R.id.pie_chart_peers_reaction);
        mChartReaction.setDescription("");
        mChartReaction.setUsePercentValues(true);
        mChartReaction.setDrawHoleEnabled(true);
        mChartReaction.setHoleRadius(7);
        mChartReaction.setTransparentCircleRadius(10);
        mChartReaction.setRotationAngle(0);
        mChartReaction.setRotationEnabled(true);
        mChartReaction.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e == null) return;
                Toast.makeText(getActivity(), String.format("You are at the %fth percentile", (double) 100*percentile_reaction/stroop_reaction_datapool.length),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });

        addDataReaction();

        Legend l = mChartReaction.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    private int findReactionPercentile(double reaction_time) {
        int i = 0;
        while(i < stroop_reaction_datapool.length){
            if(reaction_time < stroop_reaction_datapool[i])
                i++;
        }
        if(i+1 >= stroop_reaction_datapool.length)
            return stroop_reaction_datapool.length;
        return i+1;

    }

    private void addDataReaction() {
        final int percentile_reaction = findReactionPercentile(profileData.getReaction_time());

        ArrayList<Entry> yVals = new ArrayList<>();
        /*
        for(int i = 0; i < yData.length; i++){
            yVals.add(new Entry(yData[i], i));
        }*/
        yVals.add(new Entry((float) 100*percentile_reaction/stroop_reaction_datapool.length, 0));
        yVals.add(new Entry((float) (100-100*percentile_reaction/stroop_reaction_datapool.length), 1));

        ArrayList<String> xVals = new ArrayList<>();
        /*
        for(int i = 0; i < xData.length; i++){
            xVals.add(xData[i]);
        }*/
        xVals.add(profileData.getUsername());
        xVals.add("Peers' Reaction Score");

        PieDataSet dataSet = new PieDataSet(yVals, "Reaction Score vs. Peers");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for(int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChartReaction.setData(data);
        //undo all highlights
        mChartReaction.highlightValues(null);
        mChartReaction.animateXY(2000,2000);
        mChartReaction.invalidate();
    }

    private void initAccuracy() {
        final int accuracy_percentile = findAccuracyPercentile(profileData.getAccuracy());

        mChartAccuracy = (PieChart) getActivity().findViewById(R.id.pie_chart_peers_accuracy);
        mChartAccuracy.setDescription("");
        mChartAccuracy.setUsePercentValues(true);
        mChartAccuracy.setDrawHoleEnabled(true);
        mChartAccuracy.setHoleRadius(7);
        mChartAccuracy.setTransparentCircleRadius(10);
        mChartAccuracy.setRotationAngle(0);
        mChartAccuracy.setRotationEnabled(true);

        addAccuracyData();
        mChartAccuracy.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e == null) return;
                Toast.makeText(getActivity(), String.format("You are at the %fth percentile", (double) 100*accuracy_percentile/accuracy_data_pool.length),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
            }
        });

        Legend l = mChartAccuracy.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);
    }

    private int findAccuracyPercentile(double accuracy) {
        int i = 0;
        while(i < accuracy_data_pool.length){
            if(accuracy < accuracy_data_pool[i])
                i++;
        }

        if(i + 1 >= accuracy_data_pool.length)
            return accuracy_data_pool.length;
        return i + 1;
    }

    private void addAccuracyData(){
        ArrayList<Entry> yVals = new ArrayList<>();
        /*
        for(int i = 0; i < yData.length; i++){
            yVals.add(new Entry(yData[i], i));
        }*/
        yVals.add(new Entry((float) profileData.getAccuracy(), 0));
        yVals.add(new Entry((float) 0.8, 1));

        ArrayList<String> xVals = new ArrayList<>();
        /*
        for(int i = 0; i < xData.length; i++){
            xVals.add(xData[i]);
        }*/
        xVals.add(profileData.getUsername());
        xVals.add("Peers'Score");

        PieDataSet dataSet = new PieDataSet(yVals, "Accuracy Score vs. Peers");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for(int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChartAccuracy.setData(data);
        //undo all highlights
        mChartAccuracy.highlightValues(null);
        mChartAccuracy.animateXY(2000,2000);
        mChartAccuracy.invalidate();
    }

}
