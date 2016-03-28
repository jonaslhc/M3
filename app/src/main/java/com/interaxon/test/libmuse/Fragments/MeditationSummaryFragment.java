package com.interaxon.test.libmuse.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.MenuActivity;
import com.interaxon.test.libmuse.ProfileActivity;
import com.interaxon.test.libmuse.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by st924507 on 2016-03-27.
 */
public class MeditationSummaryFragment extends Fragment {

    BarChart mChart;
    CombinedChart mCombChart;
    String TAG = PersonalResultFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meditation_summary_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        ArrayList<ProfileData> arrayList = DatabaseHandler.getHandler().getMeditationList();
        ArrayList<ProfileData> stroopList = DatabaseHandler.getHandler().getStroopList();

        for(int i = 0; i < arrayList.size(); i++) {
            initCombGraph(arrayList.get(i).getMeditationDouble().get(arrayList.size() - 1).floatValue(), arrayList.get(i).getUsername());
        }

        if(stroopList.size() < 2){
            initBarGraph(stroopList.get(0).getAccuracy(), stroopList.get(0).getReactionTime(), 0.0, 0.0, stroopList.get(0).getUsername());
        }
        else{
            initBarGraph(stroopList.get(stroopList.size()-1).getAccuracy(), stroopList.get(stroopList.size()-1).getReactionTime(),
                    stroopList.get(stroopList.size()-2).getAccuracy(),
                    stroopList.get(stroopList.size()-2).getReactionTime(), stroopList.get(stroopList.size()-2).getUsername());
        }

    }

    private void initCombGraph(float percentile, String name){
        mCombChart.setNoDataText("No Data");
        mCombChart.setDescription("");
        mCombChart.setDrawHighlightArrow(true);
        Typeface tf = Typeface.DEFAULT;

        XAxis horizontal_axis = mCombChart.getXAxis();
        YAxis vertical_axis = mCombChart.getAxis(YAxis.AxisDependency.LEFT);

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


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(name);


        CombinedData data = new CombinedData(xVals);

        data.setData(generateBarData(percentile, name));
        data.setData(generateLineData(percentile, name));


        //mData.setValueFormatter(new PercentFormatter());
        mCombChart.setData(data);
        mCombChart.setDrawValueAboveBar(true);
        mCombChart.animateX(2000);
        mCombChart.animateY(2000);
        mCombChart.setDrawHighlightArrow(true);
        mCombChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.BLACK);
        l.setTextSize(12f);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);

    }

    private LineData generateLineData(float percentile, String name) {
        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();


        entries.add(new Entry(percentile, 0));

        LineDataSet set = new LineDataSet(entries, "Line DataSet");
        set.setColor(Color.rgb(240, 238, 70));
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData(double percentile, String name) {
        BarData d = new BarData();

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        entries.add(new BarEntry((float)percentile, 0));

        BarDataSet set = new BarDataSet(entries, "Bar DataSet");
        set.setColor(Color.rgb(60, 220, 78));
        set.setValueTextColor(Color.rgb(60, 220, 78));
        set.setValueTextSize(10f);
        d.addDataSet(set);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return d;
    }

    private void initBarGraph(double accuracy_data1, double reaction_time_data1, double accuracy_data2, double reaction_time_data2, String name) {

        mChart.setNoDataText("No data is currently available");
        mChart.setDescription("");
        mChart.setDrawHighlightArrow(true);

        Log.e(TAG, "current user name: " + DatabaseHandler.getHandler().getCurrUser().getUsername());
        Typeface tf = Typeface.DEFAULT;


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


        /*
        // Populate with values
        accuracy.add(new BarEntry((float) profileData.getAccuracy(), 0));
        reaction_time.add(new BarEntry((float) profileData.getReactionTime(), 0));
        */
        accuracy.add(new BarEntry((float) accuracy_data1, 0));
        accuracy.add(new BarEntry((float) accuracy_data2, 0));
        reaction_time.add(new BarEntry((float) reaction_time_data1, 0));
        reaction_time.add(new BarEntry((float) reaction_time_data2, 0));


        BarDataSet accuracy_set = new BarDataSet(accuracy, "Accuracy");
        BarDataSet reaction_score = new BarDataSet(reaction_time, "Reaction Score");

        accuracy_set.setAxisDependency(YAxis.AxisDependency.LEFT);
        accuracy_set.setColor(ColorTemplate.getHoloBlue());
        //accuracy_set.setHighLightColor(Color.rgb(244, 117, 117));
        accuracy_set.setValueTextColor(Color.BLACK);
        accuracy_set.setValueTextSize(10f);
        accuracy_set.setDrawValues(false);

        reaction_score.setAxisDependency(YAxis.AxisDependency.LEFT);
        reaction_score.setColor(Color.MAGENTA);
        //reaction_score.setHighLightColor(Color.rgb(244, 117, 117));
        reaction_score.setValueTextColor(Color.BLACK);
        reaction_score.setValueTextSize(10f);
        reaction_score.setDrawValues(false);


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(accuracy_set);
        dataSets.add(reaction_score);


        ArrayList<String> xVals = new ArrayList<String>();
        //xVals.add(arrayList.get(0).getName());
        xVals.add(name);
        xVals.add(name);

        Log.e(TAG, "Name " + name + " Accuracy " + accuracy_data1 + " Reaction " + reaction_time_data1);

        BarData mData = new BarData(xVals, dataSets);
        //mData.setValueFormatter(new PercentFormatter());
        mData.setValueTextSize(10f);
        mData.setValueTextColor(Color.BLACK);
        mChart.setDrawValueAboveBar(true);
        mChart.setData(mData);
        mChart.animateX(2000);
        mChart.animateY(2000);
        mChart.setDrawHighlightArrow(true);
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.BLACK);
        l.setTextSize(12f);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
    }



    private void initView() {
        mChart = (BarChart) getActivity().findViewById(R.id.summary_bar_chart);
        mCombChart = (CombinedChart) getActivity().findViewById(R.id.summary_comb_chart);
    }



}
