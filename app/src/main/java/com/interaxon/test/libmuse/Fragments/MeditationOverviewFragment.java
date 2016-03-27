package com.interaxon.test.libmuse.Fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeditationOverviewFragment extends Fragment implements OnChartValueSelectedListener {

    private LineChart mLineChart;
    private LineData mLineData;

    public MeditationOverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meditation_overview, container, false);

        ArrayList<ProfileData> arrayList = DatabaseHandler.getHandler().getMeditationList();

        Log.d("MEDITATION SIZE", String.valueOf(arrayList.size()));

        for (int i=0; i<arrayList.size(); i++){
            Log.d("MEDITATION SESSIONS", String.valueOf(arrayList.get(i).getMeditationSessionNum()));
            Log.d("MEDITATION", arrayList.get(i).getMeditation());

        }

        mLineChart = (LineChart) view.findViewById(R.id.chart1);
        mLineChart.setOnChartValueSelectedListener(this);

        return view;
    }

    public void graphResults () {

        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);

        // set an alternative background color
        mLineChart.setBackgroundColor(Color.WHITE);

        Typeface tf = Typeface.DEFAULT;

        ArrayList<Double> meditationData = DatabaseHandler.getHandler().getLatestMeditation();

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < meditationData.size(); i++) {
            xVals.add("");
        }

        double largest = 0;
        double smallest = meditationData.get(0);
        double curr = 0;

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> yValsAvg = new ArrayList<Entry>();

        double avg = MuseHandler.getHandler().getCalibratedMean();

        for (int i = 0; i < meditationData.size(); i++) {
            curr = meditationData.get(i).floatValue();
            if (curr > largest) largest = curr;
            else if (curr < smallest) smallest = curr;

            if (avg > largest) largest = avg;
            else if (avg < smallest) smallest = avg;

            yVals.add(new Entry((float)curr, i));
            yValsAvg.add(new Entry((float)avg, i));

        }

        if (Math.abs(largest-avg) < Math.abs(avg-smallest)) {
            largest = avg+Math.abs(avg-smallest);
        } else {
            smallest = avg-Math.abs(largest-avg);
        }

        LineDataSet set = new LineDataSet(yVals, "Meditation");
        LineDataSet setAvg = new LineDataSet(yValsAvg, "Calibration");

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(1f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);

        setAvg.setAxisDependency(YAxis.AxisDependency.LEFT);
        setAvg.setColor(Color.BLACK);
        setAvg.setCircleColor(Color.WHITE);
        setAvg.setLineWidth(2f);
        setAvg.setCircleRadius(1f);
        setAvg.setFillAlpha(65);
        setAvg.setFillColor(ColorTemplate.getHoloBlue());
        setAvg.setHighLightColor(Color.rgb(244, 117, 117));
        setAvg.setValueTextColor(Color.WHITE);
        setAvg.setValueTextSize(9f);
        setAvg.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set); // add the datasets
        dataSets.add(setAvg); // add the datasets

        // create a data object with the datasets
        mLineData = new LineData(xVals, dataSets);

        // set data
        mLineChart.setData(mLineData);

        // get the legend (only possible after setting data)
        Legend l = mLineChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.WHITE);

        XAxis xl = mLineChart.getXAxis();
        xl.setTypeface(tf);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(true);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.WHITE);

        leftAxis.setAxisMaxValue(((float)largest));
        leftAxis.setAxisMinValue((float)smallest);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }


}
