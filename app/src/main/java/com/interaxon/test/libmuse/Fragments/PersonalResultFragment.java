package com.interaxon.test.libmuse.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.MenuActivity;
import com.interaxon.test.libmuse.R;

import java.util.ArrayList;

/**
 * Created by st924507 on 2016-03-27.
 */
public class PersonalResultFragment extends Fragment {

    BarChart mChart;
    Spinner mSessionSpinner;
    static ProfileData profileData;
    double last_accuracy, last_reaction_time;
    String TAG = PersonalResultFragment.class.getSimpleName();
    int session_index;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_result_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initBarGraph();
    }


    private void initBarGraph() {

        mChart.setNoDataText("No data is currently available");
        mChart.setDescription("");
        mChart.setDrawHighlightArrow(true);

        Log.e(TAG, "current user name: " + DatabaseHandler.getHandler().getCurrUser().getUsername());
        Typeface tf = Typeface.DEFAULT;




        ArrayList<String> numSession = new ArrayList<>();
        ArrayList<ProfileData> arrayList = DatabaseHandler.getHandler().getStroopList();



        /*
        for(int i = 0; i < arrayList.size(); i ++){
            numSession.add("Session " + String.valueOf(i));
        }*/

        numSession.add("Session 0");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, numSession);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSessionSpinner.setAdapter(adapter);


        mSessionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                session_index = (int) parent.getItemIdAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        last_accuracy = arrayList.get(0).getAccuracy();
        last_reaction_time = arrayList.get(0).getReactionTime();


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
        accuracy.add(new BarEntry((float) arrayList.get(0).getAccuracy(), 0));
        reaction_time.add(new BarEntry((float) arrayList.get(0).getReactionTime(), 0));

        Log.e(TAG, "Session number" + arrayList.get(0).getMeditationSessionNum()  + "Accuracy = " + arrayList.get(0).getAccuracy());


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
        xVals.add("Jonas");

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
        mChart = (BarChart) getActivity().findViewById(R.id.profile_bar_chart);
        mSessionSpinner = (Spinner) getActivity().findViewById(R.id.session_spinner);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, )
    }
}
