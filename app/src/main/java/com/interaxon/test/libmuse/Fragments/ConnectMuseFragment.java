package com.interaxon.test.libmuse.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.test.libmuse.CalibrationActivity;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import org.w3c.dom.Text;

import java.util.List;

public class ConnectMuseFragment extends Fragment {

    TextView connectedSign;

    public ConnectMuseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_muse, container, false);

        connectedSign = (TextView) view.findViewById(R.id.connected);
        connectedSign.setTextColor(getResources().getColor(R.color.Grey));

        MuseHandler.getHandler().connect();
        checkConnectionStatus();

        return view;
    }

    public void checkConnectionStatus () {

        new Thread(new Runnable() {

            @Override
            public void run() {


                while (!MuseHandler.getHandler().getConnectionStatus()){}
                connectedSign.post(new Runnable() {
                    @Override
                    public void run() {
                        connectedSign.setTextColor(getResources().getColor(R.color.Blue));
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                        new SignalQualityFragment()).commit();
            }
        }).start();
    }

}
