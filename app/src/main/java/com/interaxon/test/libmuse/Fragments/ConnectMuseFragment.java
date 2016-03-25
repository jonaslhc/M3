package com.interaxon.test.libmuse.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.test.libmuse.CalibrationActivity;
import com.interaxon.test.libmuse.MenuActivity;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import org.w3c.dom.Text;

import java.util.List;

public class ConnectMuseFragment extends Fragment {

    TextView connectedSign;
    TextView bluetoothSign;
    Button connectButton;

    public ConnectMuseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_muse, container, false);

        connectedSign = (TextView) view.findViewById(R.id.connected);
        connectedSign.setText(getResources().getString(R.string.connecting));
        connectedSign.setTextColor(getResources().getColor(R.color.Grey));

        bluetoothSign = (TextView) view.findViewById(R.id.bluetooth);
        bluetoothSign.setVisibility(View.INVISIBLE);

        connectButton = (Button) view.findViewById(R.id.b_connect_again);
        connectButton.setVisibility(View.INVISIBLE);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });

        connect();

        return view;
    }

    public void connect () {
        if (MuseHandler.getHandler().connect()) {
            connectedSign.setText(getResources().getString(R.string.connecting));
            connectedSign.setTextColor(getResources().getColor(R.color.Grey));
            bluetoothSign.setVisibility(View.INVISIBLE);
            connectButton.setVisibility(View.INVISIBLE);

            checkConnectionStatus();
        } else {
            noBluetooth();
        }
    }

    public void checkConnectionStatus () {

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (MuseHandler.getHandler().getConnectionStatus() == ConnectionState.CONNECTING){}

                if (MuseHandler.getHandler().getConnectionStatus() == ConnectionState.CONNECTED) {
                    connectedSign.post(new Runnable() {
                        @Override
                        public void run() {
                            connectedSign.setText(getResources().getString(R.string.connected));
                            connectedSign.setTextColor(getResources().getColor(R.color.Blue));
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                    getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                            new SignalQualityFragment()).commit();
                } else {
                    Log.d("connection", "disconnected");
                    connectedSign.post(new Runnable() {
                        @Override
                        public void run() {
                            noConnection();
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();
    }

    public void noBluetooth () {
        bluetoothSign.setText(getResources().getString(R.string.bluetooth));
        connectButton.setVisibility(View.VISIBLE);
    }

    public void noConnection () {
        connectedSign.setText(getResources().getString(R.string.connectfail));
        connectButton.setVisibility(View.VISIBLE);
    }
}
