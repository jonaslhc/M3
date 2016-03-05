package com.interaxon.test.libmuse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MeditationActivity extends FragmentActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        getSupportFragmentManager().beginTransaction().add(R.id.frag_container_med,
                new StartMeditationFragment()).addToBackStack(null).commit();

    }

}
