package com.interaxon.test.libmuse;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;

import com.interaxon.test.libmuse.Fragments.MeditationOverviewFragment;

public class OverviewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,
                new MeditationOverviewFragment()).addToBackStack(null).commit();
    }



}
