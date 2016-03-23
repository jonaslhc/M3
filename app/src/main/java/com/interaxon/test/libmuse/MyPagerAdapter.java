package com.interaxon.test.libmuse;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

/**
 * Created by st924507 on 2016-03-23.
 */
public class MyPagerAdapter extends FragmentPagerAdapter{

    public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                StroopPersonalScore s1 = new StroopPersonalScore();
                return s1;
            case 1:
                StroopPeersScore s2 = new StroopPeersScore();
                return s2;

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Your Score";
            case 1:
                return "How You Compare";
        }
        return null;
    }

}
