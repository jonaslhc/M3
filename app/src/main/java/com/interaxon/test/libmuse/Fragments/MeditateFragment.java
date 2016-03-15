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

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                stopPlayback();
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                startPlayback();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudioManager.abandonAudioFocus(afChangeListener);
                stopPlayback();
            }
        }
    };

    public MeditateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meditate, container, false);

        ArrayList<Double> meditation_results;

        counterStatus = (TextView) view.findViewById(R.id.count_down_med);
        messageDisplay = (TextView) view.findViewById(R.id.meditate);

        this.getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAudioManager = (AudioManager) this.getActivity().getSystemService(Context.AUDIO_SERVICE);

        doMeditation();

        finish = false;

        return view;
    }

    public void doMeditation() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                //double start_time = System.currentTimeMillis();
                //double end_time = System.currentTimeMillis();


                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {}
                messageDisplay.post(new Runnable() {
                    @Override
                    public void run() {
                        messageDisplay.setText(getResources().getString(R.string.start_meditate));
                    }
                });
                startAudio();
                gatherData();

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
                finish = true;

                getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                        new GraphFragment()).commit();
            }
        }).start();
    }

    public void gatherData () {
        final ArrayList<Double> meditation = new ArrayList<Double>();

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (!finish) {
                    double currMean = MuseHandler.getHandler().getTotalMean();
                    if (!Double.isNaN(currMean)) {
                        meditation.add(currMean);
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                }
                DatabaseHandler.getHandler().updateMeditation(meditation);

            }
        }).start();


    }

    public void startAudio () {
        final int audioReq = mAudioManager.requestAudioFocus(afChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage(R.string.low_volume_message).setTitle(R.string.dialog_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    playAudio(audioReq);
                }
            }).create().show();
        } else {
            playAudio(audioReq);
        }
    }

    public void playAudio(int audioReq){

        if (audioReq == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            startPlayback();
        }
    }

    private void startPlayback () {
        mMediaPlayer = MediaPlayer.create(this.getActivity(), R.raw.meditation_0);
        mMediaPlayer.setLooping(false);
        mMediaPlayer.start();
    }

    private void stopPlayback () {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

}
