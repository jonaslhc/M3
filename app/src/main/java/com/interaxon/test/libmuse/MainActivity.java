package com.interaxon.test.libmuse; /**
 * Example of using libmuse library on android.
 * Interaxon, Inc. 2015
 */

import java.io.File;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.interaxon.libmuse.AnnotationData;
import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Eeg;
import com.interaxon.libmuse.LibMuseVersion;
import com.interaxon.libmuse.MessageType;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConfiguration;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseFileFactory;
import com.interaxon.libmuse.MuseFileReader;
import com.interaxon.libmuse.MuseFileWriter;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;
import com.interaxon.libmuse.MuseVersion;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Museheadband.MuseHandler;

import org.apache.commons.math3.stat.descriptive.moment.Mean;


/**
 * In this simple example com.interaxon.test.libmuse.MainActivity implements 2 MuseHeadband listeners
 * and updates UI when data from Muse is received. Similarly you can implement
 * listers for other data or register same listener to listen for different type
 * of data.
 * For simplicity we create Listeners as inner classes of com.interaxon.test.libmuse.MainActivity. We pass
 * reference to com.interaxon.test.libmuse.MainActivity as we want listeners to update UI thread in this
 * example app.
 * You can also connect multiple muses to the same phone and register same
 * listener to listen for data from different muses. In this case you will
 * have to provide synchronization for data members you are using inside
 * your listener.
 *
 * Usage instructions:
 * 1. Enable bluetooth on your device
 * 2. Pair your device with muse
 * 3. Run this project
 * 4. Press Refresh. It should display all paired Muses in Spinner
 * 5. Make sure Muse headband is waiting for connection and press connect.
 * It may take up to 10 sec in some cases.
 * 6. You should see EEG and accelerometer data as well as connection status,
 * Version information and MuseElements (alpha, beta, theta, delta, gamma waves)
 * on the screen.
 */


// Random comment
public class MainActivity extends Activity implements OnClickListener {
    /**
     * Connection listener updates UI with new connection status and logs it.
     */
    String TAG = "MainActivity";

    long calibrate_interval = 20000;
    long count_down_interval = 1000;
    CounterClass counterClass = new CounterClass(calibrate_interval, count_down_interval);
    TextView countDownTextView;
    TextView calibratedMean;
    TextView meanText;
    TextView currMean;

    double calibrated_result;


    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MuseHandler.initHandler(this);

        Button refreshButton = (Button) findViewById(R.id.refresh);
        refreshButton.setOnClickListener(this);
        Button connectButton = (Button) findViewById(R.id.connect);
        connectButton.setOnClickListener(this);
        Button disconnectButton = (Button) findViewById(R.id.disconnect);
        disconnectButton.setOnClickListener(this);
        Button pauseButton = (Button) findViewById(R.id.pause);
        pauseButton.setOnClickListener(this);
        Button calibrate = (Button) findViewById(R.id.calibrate_button);
        calibrate.setOnClickListener(this);

        countDownTextView = (TextView) findViewById(R.id.text_view_time);
        calibratedMean = (TextView) findViewById(R.id.mean);
        meanText = (TextView) findViewById(R.id.mean_text_view);


        currMean = (TextView) findViewById(R.id.current_mean);



        // // Uncomment to test Muse File Reader
        //
        // // file can be big, read it in a separate thread
        // Thread thread = new Thread(new Runnable() {
        //     public void run() {
        //         playMuseFile("testfile.muse");
        //     }
        // });
        // thread.start();

        updateMean();
    }

    public void updateMean () {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {

                    }
                    handler2.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        Spinner musesSpinner = (Spinner) findViewById(R.id.muses_spinner);
        if (v.getId() == R.id.refresh) {

            // refresh muse
            MuseManager.refreshPairedMuses();
            List<Muse> pairedMuses = MuseManager.getPairedMuses();

            // list of all the muse items
            List<String> spinnerItems = new ArrayList<String>();
            for (Muse m: pairedMuses) {
                String dev_id = m.getName() + "-" + m.getMacAddress();
                Log.i("Muse Headband", dev_id);
                spinnerItems.add(dev_id);
            }
            ArrayAdapter<String> adapterArray = new ArrayAdapter<String> (
                    this, android.R.layout.simple_spinner_item, spinnerItems);
            musesSpinner.setAdapter(adapterArray);

        } else if (v.getId() == R.id.connect) {

            // check to see if any muse is connected
            List<Muse> pairedMuses = MuseManager.getPairedMuses();
            if (pairedMuses.size() < 1) {

                Log.w(TAG, "There is nothing to connect to");

            } else {

                // pair muse
                int museNum = musesSpinner.getSelectedItemPosition();
                MuseHandler.getHandler().connect(pairedMuses.get(museNum));
            }

            TextView statusText =
                    (TextView) findViewById(R.id.con_status);
            statusText.setText(MuseHandler.getHandler().getStatus());

        } else if (v.getId() == R.id.disconnect) {

            MuseHandler.getHandler().disconnect();

        } else if (v.getId() == R.id.pause) {

            MuseHandler.getHandler().pause();

        } else if (v.getId() == R.id.calibrate_button) {

            calibrateMuse();
        }
    }

    public void calibrateMuse () {

        counterClass.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                long start_time = System.currentTimeMillis();
                long end_time = start_time + calibrate_interval;
                MuseHandler.getHandler().startAvgMean();

                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e){

                }

                handler.sendEmptyMessage(0);
            }
        }).start();

        Toast.makeText(MainActivity.this, "Calibration starts.", Toast.LENGTH_SHORT).show();
    }

    /*
     * Simple example of getting data from the "*.muse" file
     */
    private void playMuseFile(String name) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, name);
        final String tag = "Muse File Reader";
        if (!file.exists()) {
            Log.w(tag, "file doesn't exist");
            return;
        }
        MuseFileReader fileReader = MuseFileFactory.getMuseFileReader(file);
        while (fileReader.gotoNextMessage()) {
            MessageType type = fileReader.getMessageType();
            int id = fileReader.getMessageId();
            long timestamp = fileReader.getMessageTimestamp();
            Log.i(tag, "type: " + type.toString() +
                    " id: " + Integer.toString(id) +
                    " timestamp: " + String.valueOf(timestamp));
            switch(type) {
                case EEG: case BATTERY: case ACCELEROMETER: case QUANTIZATION:
                    MuseDataPacket packet = fileReader.getDataPacket();
                    Log.i(tag, "data packet: " + packet.getPacketType().toString());
                    break;
                case VERSION:
                    MuseVersion version = fileReader.getVersion();
                    Log.i(tag, "version" + version.getFirmwareType());
                    break;
                case CONFIGURATION:
                    MuseConfiguration config = fileReader.getConfiguration();
                    Log.i(tag, "config" + config.getBluetoothMac());
                    break;
                case ANNOTATION:
                    AnnotationData annotation = fileReader.getAnnotation();
                    Log.i(tag, "annotation" + annotation.getData());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CounterClass extends CountDownTimer{

        public CounterClass(long millisInFuture, long countDownInterval){
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            long millis = millisUntilFinished;
            String hms = String.format("%02d", TimeUnit.MILLISECONDS.toSeconds(millis)
                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
            countDownTextView.setText(hms);
        }

        @Override
        public void onFinish() {

            countDownTextView.setText("Completed.");
        }
    }




    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            counterClass.cancel();
            calibrated_result = MuseHandler.getHandler().getAvgMean();
            calibratedMean.setText(String.format("Calibrated:%6.4f", calibrated_result));
            meanText.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Calibration Completed.", Toast.LENGTH_SHORT).show();
        }
    };

    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            if (MuseHandler.getHandler().getTotalMean() > calibrated_result)

            {
                meanText.setText("Good ");
                meanText.setTextColor(Color.parseColor("#ff00ff00"));
            } else

            {
                meanText.setText("Bad ");
                meanText.setTextColor(Color.parseColor("#ffff0000"));
            }

            currMean.setText(String.format("Current:%6.4f", MuseHandler.getHandler().getAvgMean()));

            //TextView elem1 = (TextView) findViewById(R.id.elem1);
            //TextView elem2 = (TextView) findViewById(R.id.elem2);
            //TextView elem3 = (TextView) findViewById(R.id.elem3);
            //TextView elem4 = (TextView) findViewById(R.id.elem4);

            //TP9 = Left ear   FP1 = Left Forehead    FP2 = Right Forehead    TP10 = Right Ear
            //elem1.setText(String.format("%6.4f", tp9Mean.getResult()));
            //elem2.setText(String.format("%6.4f", fp1Mean.getResult()));
            //elem3.setText(String.format("%6.4f", fp2Mean.getResult()));
            //elem4.setText(String.format("%6.4f", tp10Mean.getResult()));
        }
    };
}
