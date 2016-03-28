package com.interaxon.test.libmuse.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Muse;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeditateFragment extends Fragment {

    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;

    boolean finish;

    TextView counterStatus;
    TextView messageDisplay;

    int session_num;

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

    public MeditateFragment() {
        // Required empty public constructor
    }

    // meditation sessions differ by audio ONLY
    // send session # and determine which audio
    public static MeditateFragment newInstance(int session_num) {
        MeditateFragment fragment = new MeditateFragment();
        Bundle args = new Bundle();
        args.putInt("session_num", session_num);
        fragment.setArguments(args);
        return fragment;
    }

    public void getSessionNum() {
        session_num = getArguments().getInt("session_num", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meditate, container, false);

        getSessionNum();

        counterStatus = (TextView) view.findViewById(R.id.count_down_med);
        messageDisplay = (TextView) view.findViewById(R.id.meditate);

        this.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager = (AudioManager) this.getActivity().getSystemService(Context.AUDIO_SERVICE);

        doMeditation();

        finish = false;

        return view;
    }

    public void doMeditation() {

        if (mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage(R.string.low_volume_message).setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    runAudio.start();
                }
            }).create().show();
        } else {
            runAudio.start();
        }

    }

    Thread runAudio = new Thread(new Runnable() {

        @Override
        public void run() {
            MuseHandler.getHandler().resume();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {}
            messageDisplay.post(new Runnable() {
                @Override
                public void run() {
                    messageDisplay.setText(getResources().getString(R.string.start_meditate));
                }
            });

            int audioReq = mAudioManager.requestAudioFocus(afChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);

            playAudio(audioReq);
            gatherData();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}

            for (int i=30; i>=0; i--) {
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

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {}


            finish = true;
            MuseHandler.getHandler().pause();


        }
    });

    public void gatherData () {
        final ArrayList<Double> meditation = new ArrayList<Double>();

        new Thread(new Runnable() {

            @Override
            public void run() {
                double percentGood = 0.0;

                while (!finish) {
                    MuseHandler.getHandler().clearMean();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    double currMean = MuseHandler.getHandler().getTotalMean();
                    if (!Double.isNaN(currMean)) {
                        meditation.add(currMean);
                        if (currMean > MuseHandler.getHandler().getCalibratedMean()) {
                            percentGood += 1.0;
                        }

                    }
                }

                meditation.add(MuseHandler.getHandler().getCalibratedMean());
                percentGood = percentGood/meditation.size();
                meditation.add(percentGood);
                DatabaseHandler.getHandler().addMeditation(meditation, session_num);
                getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                        new GraphFragment()).commit();

            }
        }).start();


    }

    public void playAudio(int audioReq){

        if (audioReq == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            startPlayback();
        }
    }

    private void startPlayback () {
        if (session_num == 1) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_s1);
        else if (session_num == 2) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 3) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 4) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 5) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 6) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 7) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 8) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 9) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_1);
        else if (session_num == 10) mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_s10);

        mMediaPlayer.setLooping(false);
        mMediaPlayer.start();
    }

    private void stopPlayback () {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

}
