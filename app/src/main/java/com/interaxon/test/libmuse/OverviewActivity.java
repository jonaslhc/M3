package com.interaxon.test.libmuse;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.interaxon.test.libmuse.Fragments.MeditationOverviewFragment;

public class OverviewActivity extends FragmentActivity {
    ViewPager viewPager;
    FragmentPagerAdapter fragmentPagerAdapter;
    PagerTabStrip pagerTabStrip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        /*
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,
                new MeditationOverviewFragment()).addToBackStack(null).commit();
                */

        ProfilePagerAdapter profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.profile_pager);
        viewPager.setAdapter(profilePagerAdapter);

        pagerTabStrip = (PagerTabStrip) findViewById(R.id.profile_tab_strip);
        pagerTabStrip.setTextColor(Color.BLACK);


    }
}
