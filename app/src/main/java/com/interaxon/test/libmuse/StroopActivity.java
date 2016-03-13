package com.interaxon.test.libmuse;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.interaxon.test.libmuse.StroopQuestions.Question1;

/**
 * Created by st924507 on 2016-03-08.
 */
public class StroopActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stroop);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new Question1()).commit();
        }
    }
}