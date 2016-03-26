package com.interaxon.test.libmuse.StroopQuestions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.MeditationActivity;
import com.interaxon.test.libmuse.MenuActivity;
import com.interaxon.test.libmuse.R;
import com.interaxon.test.libmuse.StroopActivity;
import com.interaxon.test.libmuse.StroopInfo.StroopPersonalScore;
import com.interaxon.test.libmuse.StroopInfo.StroopTabResult;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.w3c.dom.Text;

/**
 * Created by st924507 on 2016-03-09.
 */
public class FinalScore extends Fragment {

    String TAG = FinalScore.class.getSimpleName();
    TextView correct_answer, reaction_incongruent, reaction_neutral, reaction_result;
    int q2_incong_ans, q5_incong_ans, q3_neutral_ans, q6_neutral_ans;
    long q2_incong_time, q5_incong_time, q3_neutral_time, q6_neutral_time;
    double incong_score, neutral_score;
    Mean incongruent_mean = new Mean();
    Mean neutral_mean = new Mean();
    Button play_again_button, back_to_menu, go_to_results;
    MenuActivity menuActivity;
    String user_name;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stroop_score_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        menuActivity = new MenuActivity();
        clearFirstTimeUser();
        initView();
    }

    private void clearFirstTimeUser () {
        DatabaseHandler.getHandler().updateFirst();
    }

    private void initView() {
        correct_answer = (TextView) getActivity().findViewById(R.id.correct_answer);
        reaction_incongruent = (TextView) getActivity().findViewById(R.id.reaction_incongruent);
        reaction_neutral = (TextView) getActivity().findViewById(R.id.reaction_neutral);
        reaction_result = (TextView) getActivity().findViewById(R.id.reaction_result);

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
        q5_incong_time = app_preferences.getLong("time_incong5", 0);
        q3_neutral_time = app_preferences.getLong("time_neutral1", 0);
        q6_neutral_time = app_preferences.getLong("time_neutral2", 0);

        incongruent_mean.increment(q2_incong_time);
        incongruent_mean.increment(q5_incong_time);
        neutral_mean.increment(q3_neutral_time);
        neutral_mean.increment(q6_neutral_time);

        incong_score = (double) (q2_incong_ans + q5_incong_ans)/2.0;
        neutral_score = (double) (q3_neutral_ans + q6_neutral_ans)/2.0;
        Log.e(TAG," incongruent score " + incong_score + " neutral " + neutral_score);

        // divide by 0 case
        if(neutral_score == 0)
            neutral_score = 1;

        correct_answer.setText(String.format("%6.0f%%", (float) ((float)test / 6.0 * 100)));
        reaction_incongruent.setText(String.format("%6.2f", (incongruent_mean.getResult() / neutral_mean.getResult())));

        // update accuracy with specified user_name
        double reaction_time = (incongruent_mean.getResult()/neutral_mean.getResult());
        double accuracy = incong_score / neutral_score;

        DatabaseHandler.getHandler().addStroop(reaction_time, accuracy);

        //user_name = DatabaseHandler.getHandler().getCurrUser().getName();
        //db.updateAccuracy(incong_score / neutral_score, user_name);
        //db.updateReactionTime((incongruent_mean.getResult()/neutral_mean.getResult()), user_name);


        play_again_button = (Button) getActivity().findViewById(R.id.b_play_again);
        play_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StroopActivity.class);
                startActivity(intent);
            }
        });

        back_to_menu = (Button) getActivity().findViewById(R.id.b_back_menu);
        back_to_menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuActivity.class);
                startActivity(intent);
            }
        });

        go_to_results = (Button) getActivity().findViewById(R.id.see_result);
        go_to_results.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StroopTabResult.class);
                startActivity(intent);
            }
        });


    }
}
