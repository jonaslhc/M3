package com.interaxon.test.libmuse.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.interaxon.libmuse.ConnectionState;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.MenuActivity;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class GraphFragment extends Fragment implements OnChartValueSelectedListener {


    private LineChart mLineChart;
    private LineData mLineData;

    Button mMenuButton;
    TextView mResultTextView;

    double percentGood;

    public GraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        mLineChart = (LineChart) view.findViewById(R.id.chart1);
        mLineChart.setOnChartValueSelectedListener(this);

        percentGood = 0.0;

        mResultTextView = (TextView) view.findViewById(R.id.meditate_result);
        //mResultTextView.setText(String.format("%6.0f%%", percentGood));

        mMenuButton = (Button) view.findViewById(R.id.b_back_menu_med);
        mMenuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                returnToMenu();
            }
        });

        graphResults();
        mResultTextView.setText(String.format("%6.0f%%", percentGood));

        return view;
    }

    public void returnToMenu () {
        Intent intent = new Intent(this.getActivity(), MenuActivity.class);
        startActivity(intent);
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

        ArrayList<Double> meditationData = DatabaseHandler.getHandler().getMeditation();

        double second = 0.0;
        double largest = 0.0;
        double smallest = meditationData.get(0);
        double curr;

        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> yValsAvg = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        double avg = MuseHandler.getHandler().getCalibratedMean();

        for (int i = 0; i < meditationData.size(); i++) {
            xVals.add(String.format("%6.1f", second));
            second += 0.5;

            curr = meditationData.get(i).floatValue();
            if (curr > largest) largest = curr;
            else if (curr < smallest) smallest = curr;

            if (avg > largest) largest = avg;
            else if (avg < smallest) smallest = avg;

            yVals.add(new Entry((float)curr, i));
            yValsAvg.add(new Entry((float)avg, i));

            if (curr > avg) {
                percentGood = percentGood + 1.0;
            }
        }

        Log.d("Data Processing", String.valueOf(percentGood));
        Log.d("Data Processing", String.valueOf((double)meditationData.size()));

        percentGood = percentGood/(double)meditationData.size()*100;

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
        set.setValueTextColor(Color.BLACK);
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
        setAvg.setValueTextColor(Color.BLACK);
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
        xl.setTextColor(Color.BLACK);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(true);

        YAxis leftAxis = mLineChart.getAxisLeft();
        //leftAxis.setTypeface(tf);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(((float)largest));
        leftAxis.setAxisMinValue((float)smallest);
        leftAxis.setDrawGridLines(false);

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
