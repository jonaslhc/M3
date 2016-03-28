package com.interaxon.test.libmuse.StroopQuestions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.interaxon.test.libmuse.R;

/**
 * Created by st924507 on 2016-03-11.
 */
public class Question5 extends Fragment {
    RadioButton q5a1,q5a2,q5a3,q5a4;
    long start_time, end_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.question5_layout, container, false);
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
        q5a1 = (RadioButton) getActivity().findViewById(R.id.q5a1);
        q5a2 = (RadioButton) getActivity().findViewById(R.id.q5a2);
        q5a3 = (RadioButton) getActivity().findViewById(R.id.q5a3);
        q5a4 = (RadioButton) getActivity().findViewById(R.id.q5a4);

        q5a1.setOnClickListener(listener);
        q5a2.setOnClickListener(listener);
        q5a3.setOnClickListener(listener);
        q5a4.setOnClickListener(listener);
    }

    Button.OnClickListener listener = new Button.OnClickListener(){
        @Override
        public void onClick(View v){
            final SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = app_preferences.edit();

            if (q5a2.isChecked()){
                editor.putInt("answer_value5", 1);
            } else {
                editor.putInt("answer_value5", 0);
            }
            end_time = System.currentTimeMillis();
            editor.putLong("time_incong2", end_time - start_time);
            editor.commit();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Question6()).commit();
        }
    };
}
