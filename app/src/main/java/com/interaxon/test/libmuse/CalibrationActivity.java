package com.interaxon.test.libmuse; /**
 * Example of using libmuse library on android.
 * Interaxon, Inc. 2015
 */

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.interaxon.libmuse.AnnotationData;
import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.MessageType;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseConfiguration;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseFileFactory;
import com.interaxon.libmuse.MuseFileReader;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MuseVersion;
import com.interaxon.test.libmuse.Fragments.CalibrateFragment;
import com.interaxon.test.libmuse.Fragments.ConnectMuseFragment;
import com.interaxon.test.libmuse.Fragments.SignalQualityFragment;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;

public class CalibrationActivity extends FragmentActivity implements OnClickListener {

    String TAG = "CalibrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        MuseHandler.initHandler(this);

        if (MuseHandler.getHandler().getConnectionStatus() == ConnectionState.CONNECTED) {
            getSupportFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new SignalQualityFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.frag_container_med,
                    new ConnectMuseFragment()).commit();
        }
    }

    @Override
    public void onClick(View v) {

    }

}
