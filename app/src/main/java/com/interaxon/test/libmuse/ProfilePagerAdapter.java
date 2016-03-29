package com.interaxon.test.libmuse;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.interaxon.test.libmuse.Fragments.MeditateFragment;
import com.interaxon.test.libmuse.Fragments.MeditationOverviewFragment;
import com.interaxon.test.libmuse.Fragments.MeditationSummaryFragment;
import com.interaxon.test.libmuse.Fragments.PersonalResultFragment;
import com.interaxon.test.libmuse.StroopInfo.StroopPeersScore;
import com.interaxon.test.libmuse.StroopInfo.StroopPersonalScore;

import java.util.Locale;

/**
 * Created by st924507 on 2016-03-27.
 */
public class ProfilePagerAdapter extends FragmentPagerAdapter {

    public ProfilePagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                MeditationSummaryFragment s1 = new MeditationSummaryFragment();
                return s1;
            case 1:
                PersonalResultFragment s2 = new PersonalResultFragment();
                return s2;
            case 2:
                MeditationOverviewFragment s3 = new MeditationOverviewFragment();
                return s3;


        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Overview"; // Show 10 meditation data & 1st and 10th Stroop results
            case 1:
                return "Distractibility"; // same as case 2 for stroop
            case 2:
                return "Meditation";  // have ability to choose which session to see
        }
        return null;
    }
}
