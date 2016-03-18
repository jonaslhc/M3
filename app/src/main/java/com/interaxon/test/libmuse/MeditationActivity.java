package com.interaxon.test.libmuse;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.interaxon.test.libmuse.Fragments.MeditateFragment;
import com.interaxon.test.libmuse.Fragments.MeditationMenuFragment;

public class MeditationActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        getSupportFragmentManager().beginTransaction().add(R.id.frag_container_med,
                new MeditationMenuFragment()).addToBackStack("Add to Back Stack").commit();

    }


}
