package com.interaxon.test.libmuse.Fragments;


import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.test.libmuse.MeditationActivity;
import com.interaxon.test.libmuse.MenuActivity;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class CalibrateFragment extends Fragment {

    public final static String EXTRA_MESSAGE = "CALIBRATE STATUS";


    TextView calibrateStatus;
    TextView counterStatus;
    TextView resultStatus;
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;


    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                stopPlayback();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                //startPlayback();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.abandonAudioFocus(afChangeListener);
                stopPlayback();
            }
        }
    };



    public CalibrateFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibrate, container, false);

        this.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager = (AudioManager) this.getActivity().getSystemService(Context.AUDIO_SERVICE);

        calibrateStatus = (TextView) view.findViewById(R.id.calibrate);
        calibrateStatus.setTextColor(getResources().getColor(R.color.Grey));
        counterStatus = (TextView) view.findViewById(R.id.count_down);
        resultStatus = (TextView) view.findViewById(R.id.back_signalQ);
        calibrateMuse();

        return view;
    }

    public void calibrateMuse () {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                calibrateStatus.post(new Runnable() {
                    @Override
                    public void run() {
                        calibrateStatus.setText(getString(R.string.calibratestart));

                    }
                });

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {}



                //start running audio
                int audioReq = mAudioManager.requestAudioFocus(afChangeListener,
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);

                playAudio(audioReq);

                for (int i=9; i>=0; i--) {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}

                }
                MuseHandler.getHandler().clearMean();

                for (int i=20; i>=1; i--) {
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

                    if (!MuseHandler.getHandler().getDataQuality()) {
                        stopPlayback();
                        backToSignalQuality();
                        return;
                    } else if (MuseHandler.getHandler().getConnectionStatus() == ConnectionState.DISCONNECTED) {
                        stopPlayback();
                        backToConnectMuse();
                    }
                }

                MuseHandler.getHandler().setCalibratedMean();

                if (Double.isNaN(MuseHandler.getHandler().getCalibratedMean())){
                    backToSignalQuality();
                } else {
                    calibrateStatus.post(new Runnable() {
                        @Override
                        public void run() {
                            calibrateStatus.setTextColor(getResources().getColor(R.color.Blue));
                            calibrateStatus.setText(getString(R.string.calibratedone));
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }
                    finish();
                }
            }
        }).start();
    }
    public void backToSignalQuality () {
        calibrateStatus.post(new Runnable() {
            @Override
            public void run() {
                calibrateStatus.setText(getString(R.string.calibratefail));
                resultStatus.setText(getString(R.string.back_to_signalQ));
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        getFragmentManager().beginTransaction().replace(R.id.frag_container_med,
                new SignalQualityFragment()).commit();

    }

    public void backToConnectMuse () {
        calibrateStatus.post(new Runnable() {
            @Override
            public void run() {
                calibrateStatus.setText(getString(R.string.calibratedisconnect));
                resultStatus.setText(getString(R.string.back_to_connect));
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {

        }
        getFragmentManager().beginTransaction().replace(R.id.frag_container_med,
                new SignalQualityFragment()).commit();

    }

    public void finish () {
        Intent intent = new Intent(getActivity(), MeditationActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "calibrated");
        startActivity(intent);
    }


    public void playAudio(int audioReq){

        if (audioReq == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            startPlayback();
        }
    }

    private void startPlayback () {
        mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.calibration);
        mMediaPlayer.setLooping(false);
        mMediaPlayer.start();
    }

    private void stopPlayback () {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

}
