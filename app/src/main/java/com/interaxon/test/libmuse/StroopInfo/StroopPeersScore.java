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

    static double[] stroop_reaction_datapool = {1.05600015316281,
            1.29847247966081,
            1.2288096106039,
            1.16167778836988,
            1.11704597982435,
            1.11516006326332,
            1.20077605359325,
            1.1582987870413,
            1.02741349707231,
            1.37387903502193,
            1.15576642661588,
            1.45498309999757,
            1.12412927596748,
            1.10847764670362,
            1.28462128108195,
            1.25947142198571,
            1.25137182552159,
            1.17825594888198,
            1.30764079742611,
            1.51992456556947,
            1.1039816052319,
            1.2630804137167,
            1.26325715158779,
            1.13273130351659,
            1.21737603504088,
            1.23466237762948,
            1.32573627426477,
            1.20305084250735,
            1.12065840099374,
            1.1708084285873,
            1.13815593936792,
            1.12170415383674,
            1.18865126406484,
            1.34539972988066,
            1.17062220119416,
            1.08764601685178,
            1.11066409754373,
            1.14087451456816,
            1.02688193317769,
            1.16172347881491,
            1.14747450533865,
            1.14632815503611,
            1.38571429828052,
            1.1266460165299,
            1.36101258521705,
            1.14971170602132,
            1.14701616426387,
            1.12068703147899,
    };
    static double[] accuracy_data_pool = {1,
            0.99537037037037,
            0.99537037037037,
            0.99537037037037,
            1,
            0.972222222222222,
            0.981481481481482,
            0.962962962962963,
            0.99537037037037,
            0.962962962962963,
            0.967592592592593,
            0.953703703703704,
            0.967592592592593,
            0.986111111111111,
            1,
            0.972222222222222,
            0.981481481481482,
            0.99537037037037,
            0.99537037037037,
            0.930555555555556,
            1,
            0.740740740740741,
            0.990740740740741,
            0.990740740740741,
            1,
            0.949074074074074,
            0.925925925925926,
            0.99537037037037,
            0.99537037037037,
            0.953703703703704,
            1,
            0.986111111111111,
            1,
            0.972222222222222,
            0.958333333333333,
            0.986111111111111,
            0.986111111111111,
            0.981481481481482,
            0.99537037037037,
            0.99537037037037,
            0.981481481481482,
            0.865740740740741,
            0.986111111111111,
            0.976851851851852,
            0.981481481481482,
            0.930555555555555,
            1,
            0.981481481481482,
    };
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
