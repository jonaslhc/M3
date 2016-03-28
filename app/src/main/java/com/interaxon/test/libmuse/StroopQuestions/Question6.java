package com.interaxon.test.libmuse.StroopQuestions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.MenuActivity;
import com.interaxon.test.libmuse.R;
import com.interaxon.test.libmuse.StroopActivity;
import com.interaxon.test.libmuse.StroopInfo.StroopTabResult;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

/**
 * Created by st924507 on 2016-03-11.
 */
public class Question6 extends Fragment {
    RadioButton q6a1,q6a2,q6a3,q6a4;
    long start_time, end_time;

    TextView correct_answer, reaction_incongruent, reaction_neutral, reaction_result;
    int q2_incong_ans, q5_incong_ans, q3_neutral_ans, q6_neutral_ans;
    long q2_incong_time, q5_incong_time, q3_neutral_time, q6_neutral_time;
    Mean incongruent_mean = new Mean();
    Mean neutral_mean = new Mean();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question6_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start_time = System.currentTimeMillis();
        initView();
    }
    private void initView(){
        initAnswer();
    }

    private void initAnswer(){
        q6a1 = (RadioButton) getActivity().findViewById(R.id.q6a1);
        q6a2 = (RadioButton) getActivity().findViewById(R.id.q6a2);
        q6a3 = (RadioButton) getActivity().findViewById(R.id.q6a3);
        q6a4 = (RadioButton) getActivity().findViewById(R.id.q6a4);

        q6a1.setOnClickListener(listener);
        q6a2.setOnClickListener(listener);
        q6a3.setOnClickListener(listener);
        q6a4.setOnClickListener(listener);
    }

    Button.OnClickListener listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = app_preferences.edit();

            if (q6a1.isChecked()){
                editor.putInt("answer_value6", 1);
            } else {
                editor.putInt("answer_value6", 0);
            }
            end_time = System.currentTimeMillis();
            editor.putLong("time_neutral2", end_time - start_time);
            editor.commit();
            finish_stroop();

            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FinalScore()).addToBackStack(null).commit();
            Intent intent = new Intent(getActivity(), StroopTabResult.class);
            startActivity(intent);
        }
    };

    public void finish_stroop () {
        clearFirstTimeUser();

        //correct_answer = (TextView) getActivity().findViewById(R.id.correct_answer);
        //reaction_incongruent = (TextView) getActivity().findViewById(R.id.reaction_incongruent);
        //reaction_neutral = (TextView) getActivity().findViewById(R.id.reaction_neutral);
        //reaction_result = (TextView) getActivity().findViewById(R.id.reaction_result);

        final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        int test = app_preferences.getInt("answer_value1", 0)+
                app_preferences.getInt("answer_value2", 0)+
                app_preferences.getInt("answer_value3", 0)+
                app_preferences.getInt("answer_value4", 0)+
                app_preferences.getInt("answer_value5", 0)+
                app_preferences.getInt("answer_value6", 0);

        q2_incong_ans = app_preferences.getInt("answer_value2", 0);
        q5_incong_ans =  app_preferences.getInt("answer_value5", 0);
        q3_neutral_ans = app_preferences.getInt("answer_value3", 0);
        q6_neutral_ans = app_preferences.getInt("answer_value6", 0);

        q2_incong_time = app_preferences.getLong("time_incong1", 0);
        q5_incong_time = app_preferences.getLong("time_incong2", 0);
        q3_neutral_time = app_preferences.getLong("time_neutral1", 0);
        q6_neutral_time = app_preferences.getLong("time_neutral2", 0);

        incongruent_mean.increment(q2_incong_time);
        incongruent_mean.increment(q5_incong_time);
        neutral_mean.increment(q3_neutral_time);
        neutral_mean.increment(q6_neutral_time);


        //correct_answer.setText(String.format("%6.0f%%", (float) ((float)test / 6.0 *100)));
        //reaction_incongruent.setText(String.format("%6.2f", (double) (q2_incong_time + q5_incong_time) / (q3_neutral_time+q6_neutral_time)));

        // update accuracy with specified user_name
        double reaction_time = (double)(q2_incong_time + q5_incong_time) / (double)(q3_neutral_time + q6_neutral_time);
        double accuracy = ((double)test / 6.0 );

        DatabaseHandler.getHandler().addStroop(reaction_time, accuracy);
    }


    private void clearFirstTimeUser () {
        DatabaseHandler.getHandler().updateFirst();
    }

}
