package com.interaxon.test.libmuse.Fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.interaxon.test.libmuse.R;

import java.util.ArrayList;



public class MeditationMenuFragment extends Fragment implements View.OnClickListener {

    private static final int NUMSESSION = 10;

    public MeditationMenuFragment() {
        // Required empty public constructor
    }

    Button med1, med2, med3, med4, med5, med6, med7, med8, med9, med10;

    ArrayList<Button> med_session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_meditation, container, false);

        med1 = (Button) view.findViewById(R.id.b_med1);
        med2 = (Button) view.findViewById(R.id.b_med2);
        med3 = (Button) view.findViewById(R.id.b_med3);
        med4 = (Button) view.findViewById(R.id.b_med4);
        med5 = (Button) view.findViewById(R.id.b_med5);
        med6 = (Button) view.findViewById(R.id.b_med6);
        med7 = (Button) view.findViewById(R.id.b_med7);
        med8 = (Button) view.findViewById(R.id.b_med8);
        med9 = (Button) view.findViewById(R.id.b_med9);
        med10 = (Button) view.findViewById(R.id.b_med10);

        med1.setOnClickListener(this);
        med2.setOnClickListener(this);
        med3.setOnClickListener(this);
        med4.setOnClickListener(this);
        med5.setOnClickListener(this);
        med6.setOnClickListener(this);
        med7.setOnClickListener(this);
        med8.setOnClickListener(this);
        med9.setOnClickListener(this);
        med10.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick (View v) {
        if (v.getId() == R.id.b_med1) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(1)).commit();
        } else if (v.getId() == R.id.b_med2) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(2)).commit();
        } else if (v.getId() == R.id.b_med3) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(3)).commit();
        } else if (v.getId() == R.id.b_med4) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(4)).commit();
        } else if (v.getId() == R.id.b_med5) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(5)).commit();
        } else if (v.getId() == R.id.b_med6) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(6)).commit();
        } else if (v.getId() == R.id.b_med7) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(7)).commit();
        } else if (v.getId() == R.id.b_med8) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(8)).commit();
        } else if (v.getId() == R.id.b_med9) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(9)).commit();
        } else if (v.getId() == R.id.b_med10) {
            getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new MeditateFragment().newInstance(10)).commit();
        }
    }

}
