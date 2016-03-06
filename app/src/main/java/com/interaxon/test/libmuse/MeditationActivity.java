package com.interaxon.test.libmuse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.interaxon.libmuse.Eeg;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseFileWriter;
import com.interaxon.test.libmuse.Fragments.MeditationMenuFragment;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MeditationActivity extends FragmentActivity {

    Context mContext;
    long calibrate_interval = 20000;
    long count_down_interval = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        getSupportFragmentManager().beginTransaction().add(R.id.frag_container_med,
                new MeditationMenuFragment()).addToBackStack(null).commit();

    }


}
