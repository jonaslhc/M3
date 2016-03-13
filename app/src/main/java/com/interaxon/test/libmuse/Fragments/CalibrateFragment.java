package com.interaxon.test.libmuse.Fragments;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class CalibrateFragment extends Fragment {

    TextView calibrateStatus;
    TextView counterStatus;

    public CalibrateFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibrate, container, false);

        calibrateStatus = (TextView) view.findViewById(R.id.calibrate);
        calibrateStatus.setTextColor(getResources().getColor(R.color.Grey));
        counterStatus = (TextView) view.findViewById(R.id.count_down);

        calibrateMuse();

        return view;
    }

    public void calibrateMuse () {

        new Thread(new Runnable() {
            @Override
            public void run() {


                for (int i=20; i>=0; i--) {
                    final int time_left = i;
                    counterStatus.post(new Runnable() {
                        @Override
                        public void run() {
                            counterStatus.setText(String.valueOf(time_left));

                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}

                }

                MuseHandler.getHandler().startAvgMean();
                calibrateStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        calibrateStatus.setTextColor(getResources().getColor(R.color.Blue));
                    }
                });

                getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                        new CalibrateFragment()).commit();
            }
        }).start();
    }
}
