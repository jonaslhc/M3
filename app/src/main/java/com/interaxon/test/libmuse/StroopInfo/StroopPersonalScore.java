package com.interaxon.test.libmuse.StroopInfo;

import android.content.Intent;
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
import android.widget.Button;
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
 * Created by st924507 on 2016-03-23.
 */
public class StroopPersonalScore extends Fragment {


    double last_accuracy, last_reaction_time;
    static ProfileData profileData;
    TextView moreInfoText;
    String TAG = StroopPersonalScore.class.getSimpleName();
    MenuActivity menuActivity;
    BarChart mChart;
    Button backButton;

    TextView accuracyTextView, reactionTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_stroop_score_fragment_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // start_time = System.currentTimeMillis();
        menuActivity = new MenuActivity();
        initView();
        initBarGraph();
    }


    private void initBarGraph() {

        mChart.setNoDataText("No data is currently available");
        mChart.setDescription("");
        //mChart.setDrawHighlightArrow(true);

        //Log.e(TAG, "current user name: " + DatabaseHandler.getHandler().getCurrUser().getUsername());
        Typeface tf = Typeface.DEFAULT;

        profileData = DatabaseHandler.getHandler().getCurrUser();
        last_accuracy = profileData.getAccuracy();
        last_reaction_time = profileData.getReactionTime();

        accuracyTextView.setText("Your were" + String.format("%6.0f%%", last_accuracy*100) + " accurate.");
        reactionTextView.setText("Your distractibility score was" + String.format("%6.2f", last_reaction_time) + ".");

        XAxis horizontal_axis = mChart.getXAxis();
        YAxis vertical_axis = mChart.getAxis(YAxis.AxisDependency.LEFT);

        Log.d(TAG, String.valueOf(last_accuracy));
        Log.d(TAG, String.valueOf(last_reaction_time));

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

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        ArrayList<BarEntry> accuracy = new ArrayList<>();
        ArrayList<BarEntry> reaction_time = new ArrayList<>();


        // Populate with values
        accuracy.add(new BarEntry((float) profileData.getAccuracy(), 0));
        reaction_time.add(new BarEntry((float) profileData.getReactionTime(), 0));


        BarDataSet accuracy_set = new BarDataSet(accuracy, "Accuracy");
        BarDataSet reaction_score = new BarDataSet(reaction_time, "Distractibility");

        accuracy_set.setAxisDependency(YAxis.AxisDependency.LEFT);
        accuracy_set.setColor(ColorTemplate.getHoloBlue());
        //accuracy_set.setHighLightColor(Color.rgb(244, 117, 117));
        accuracy_set.setValueTextColor(Color.BLACK);
        accuracy_set.setValueTextSize(10f);
        accuracy_set.setDrawValues(true);

        reaction_score.setAxisDependency(YAxis.AxisDependency.LEFT);
        reaction_score.setColor(Color.MAGENTA);
        //reaction_score.setHighLightColor(Color.rgb(244, 117, 117));
        reaction_score.setValueTextColor(Color.BLACK);
        reaction_score.setValueTextSize(10f);
        reaction_score.setDrawValues(true);


        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(accuracy_set);
        dataSets.add(reaction_score);


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add(profileData.getName());

        BarData mData = new BarData(xVals, dataSets);
        //mData.setValueFormatter(new PercentFormatter());
        mData.setValueTextSize(10f);
        mData.setValueTextColor(Color.BLACK);
        mChart.setDrawValueAboveBar(true);
        mChart.setData(mData);
        mChart.animateX(2000);
        mChart.animateY(2000);
        mChart.setDrawValueAboveBar(true);
        mChart.setDrawHighlightArrow(true);
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.BLACK);

        /*Legend l = mChart.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.BLACK);
        l.setTextSize(12f);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);*/
    }



    private void initView() {
        mChart = (BarChart) getActivity().findViewById(R.id.bar_chart_personal);
        backButton = (Button) getActivity().findViewById(R.id.back_to_menu);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });

        moreInfoText = (TextView) getActivity().findViewById(R.id.more_info_text_view);
        accuracyTextView = (TextView) getActivity().findViewById(R.id.accuracy_result);
        reactionTextView = (TextView) getActivity().findViewById(R.id.reaction_time_result);
    }


}
