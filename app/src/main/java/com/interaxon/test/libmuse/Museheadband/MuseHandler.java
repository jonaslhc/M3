package com.interaxon.test.libmuse.Museheadband;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.interaxon.libmuse.ConnectionState;
import com.interaxon.libmuse.Eeg;
import com.interaxon.libmuse.Muse;
import com.interaxon.libmuse.MuseArtifactPacket;
import com.interaxon.libmuse.MuseConnectionListener;
import com.interaxon.libmuse.MuseConnectionPacket;
import com.interaxon.libmuse.MuseDataListener;
import com.interaxon.libmuse.MuseDataPacket;
import com.interaxon.libmuse.MuseDataPacketType;
import com.interaxon.libmuse.MuseFileFactory;
import com.interaxon.libmuse.MuseFileWriter;
import com.interaxon.libmuse.MuseManager;
import com.interaxon.libmuse.MusePreset;
import com.interaxon.libmuse.MuseVersion;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

// Handler for dealing with the Muse headband device

public class MuseHandler {

    static String TAG = "Muse Handler";

    private static MuseHandler mMuseHandler = null;
    private static Activity mActivity;
    private static Context mContext;
    private static File mDirectory;
    private ConnectionListener mConnectionListener = null;
    private DataListener mDataListener = null;

    private Muse currMuse = null;
    private boolean dataTransmission = true;
    private MuseFileWriter fileWriter = null;

    private ArrayList<Double> alphaData;
    private boolean alphaDataRdy;

    Mean tp9Mean = new Mean();
    Mean fp1Mean = new Mean();
    Mean fp2Mean = new Mean();
    Mean tp10Mean = new Mean();

    Mean avgMean = new Mean();

    Double calibratedMean = 0.0;
    Double totalMean = 0.0;

    boolean tp9Rdy, fp1Rdy, fp2Rdy, tp10Rdy;

    public MuseHandler (Activity activity) {

        WeakReference<Activity> weakActivity = new WeakReference<Activity>(activity);

        mConnectionListener = new ConnectionListener(weakActivity);
        mDataListener = new DataListener(weakActivity);

        setFileWriter();
    }

    public static MuseHandler initHandler (Activity activity) {

        mActivity = activity;

        if (mMuseHandler == null) {
            mDirectory = mActivity.getBaseContext().getFilesDir();
            mMuseHandler = new MuseHandler(activity);
            Log.d(TAG,"new handler");
            Log.d(TAG, mDirectory.getAbsolutePath());
        }

        return mMuseHandler;
    }

    public static MuseHandler getHandler () {
        return mMuseHandler;
    }

    public void setFileWriter () {
        fileWriter = MuseFileFactory.getMuseFileWriter(
                new File(mDirectory, "new_muse_file.muse"));
        fileWriter.addAnnotationString(1, "Starting muse handler.");
        mDataListener.setFileWriter(fileWriter);
    }

    public String getVersion () {
        MuseVersion museVersion = currMuse.getMuseVersion();
        String version = museVersion.getFirmwareType() +
                " - " + museVersion.getFirmwareVersion() +
                " - " + Integer.toString(
                museVersion.getProtocolVersion());

        return version;
    }

    public ConnectionState getConnectionStatus () {

        ConnectionState connectionState = mConnectionListener.mCurrent;

        return connectionState;
    }

    public void disconnect () {

        if (currMuse != null) {
            currMuse.disconnect(true);
            fileWriter.addAnnotationString(1, "Disconnect clicked.");
            fileWriter.flush();
            fileWriter.close();
        }
    }

    public void pause () {
        dataTransmission = !dataTransmission;
        if (currMuse != null) {
            currMuse.enableDataTransmission(dataTransmission);
        }
    }

    public void connect () {

        //refresh list of muses
        MuseManager.refreshPairedMuses();
        List<Muse> pairedMuses = MuseManager.getPairedMuses();

        //try to connect, if not keep trying
        //for (Muse m: pairedMuses) {
            currMuse = pairedMuses.get(0);

            Log.d(TAG, "connecting" + currMuse.getName());

            ConnectionState state = currMuse.getConnectionState();
            if (state == ConnectionState.CONNECTED ||
                    state == ConnectionState.CONNECTING) {
                Log.w("Muse Headband",
                        "doesn't make sense to connect second time to the same muse");
                return;
            }

            configureLibrary();
            fileWriter.open();
            fileWriter.addAnnotationString(1, "Connect clicked.");

            try {
                currMuse.runAsynchronously();
            } catch (Exception e) {
                Log.e("Muse Headband", e.toString());
            }
    }

    private void configureLibrary() {
        currMuse.registerConnectionListener(mConnectionListener);
        currMuse.registerDataListener(mDataListener,
                MuseDataPacketType.HORSESHOE);
        currMuse.registerDataListener(mDataListener,
                MuseDataPacketType.EEG);
        currMuse.registerDataListener(mDataListener,
                MuseDataPacketType.ALPHA_RELATIVE);
        currMuse.registerDataListener(mDataListener,
                MuseDataPacketType.BATTERY);
        currMuse.registerDataListener(mDataListener,
                MuseDataPacketType.BETA_RELATIVE);
        currMuse.setPreset(MusePreset.PRESET_14);
        currMuse.enableDataTransmission(dataTransmission);
    }

    public ArrayList<Double> getAlphaRelative () {
       return alphaData;
    }

    class ConnectionListener extends MuseConnectionListener {

        final WeakReference<Activity> activityRef;

        public ConnectionState mCurrent;
        public String mStatus;
        public String mFull;

        ConnectionListener(final WeakReference<Activity> activityRef) {
            this.activityRef = activityRef;
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket p) {
            mCurrent = p.getCurrentConnectionState();
            mStatus = p.getPreviousConnectionState().toString() + " -> " + mCurrent;
            mFull = "Muse " + p.getSource().getMacAddress() + " " + mStatus;
            Log.i("Muse Headband", mFull);

            Activity activity = activityRef.get();

            /*if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrent == ConnectionState.CONNECTED) {
                            Toast toast = Toast.makeText(mActivity.getBaseContext(), "Muse Headband Connected", Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                        }
                    }
                });
            }*/

            if (mCurrent == ConnectionState.CONNECTED) {
                Toast toast = Toast.makeText(mActivity.getBaseContext(), "Muse Headband Connected", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void startAvgMean () {
        tp10Mean.clear();
        tp9Mean.clear();
        fp1Mean.clear();
        fp2Mean.clear();
        avgMean.clear();
    }
    public void setCalibratedMean (){
        calibratedMean = avgMean.getResult();
    }

    public double getAvgMean () {
        //Log.d(TAG, String.valueOf(avgMean.getResult()));
        return calibratedMean;
    }

    public double getTotalMean () {
        return totalMean;
    }

    class DataListener extends MuseDataListener {

        final WeakReference<Activity> activityRef;
        private MuseFileWriter fileWriter;

        DataListener(final WeakReference<Activity> activityRef) {
            this.activityRef = activityRef;
        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket p) {
            alphaDataRdy = true;
            switch (p.getPacketType()) {
                case ALPHA_RELATIVE:
                    updateAlphaRelative(p.getValues());
                    updateAlphaRdy();
                    break;
                case HORSESHOE:
                    getSignalQuality(p.getValues());
                    break;
                case BATTERY:
                    fileWriter.addDataPacket(1, p);
                    if (fileWriter.getBufferedMessagesSize() > 8096)
                        fileWriter.flush();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket p) {
            if (p.getHeadbandOn() && p.getBlink()) {

            }
        }


        public void setFileWriter(MuseFileWriter fileWriter) {
            this.fileWriter = fileWriter;
        }

        public void updateAlphaRdy (){
            alphaDataRdy = true;
        }

        private void updateAlphaRelative (final ArrayList<Double> data) {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    if (data.get(Eeg.TP9.ordinal()) < 0.001|| data.get(Eeg.TP10.ordinal()) < 0.001||
                            data.get(Eeg.FP1.ordinal()) < 0.001|| data.get(Eeg.FP2.ordinal())< 0.001){
                        return;
                    }
                    tp9Mean.increment(data.get(Eeg.TP9.ordinal()));
                    fp1Mean.increment(data.get(Eeg.FP1.ordinal()));
                    fp2Mean.increment(data.get(Eeg.FP2.ordinal()));
                    tp10Mean.increment(data.get(Eeg.TP10.ordinal()));

                    totalMean = tp9Mean.getResult() + fp1Mean.getResult() +
                            fp2Mean.getResult() + tp10Mean.getResult();

                    avgMean.increment(totalMean);

                    String s = String.format("%6.4f %6.4f %6.4f %6.4f",
                            tp9Mean.getResult(), fp1Mean.getResult(),
                            fp2Mean.getResult(), tp10Mean.getResult());

                    String s2 = String.format("%6.4f %6.4f %6.4f %6.4f",
                            data.get(Eeg.TP9.ordinal()), data.get(Eeg.FP1.ordinal()),
                            data.get(Eeg.FP2.ordinal()), data.get(Eeg.TP10.ordinal()));

                    //Log.d(TAG, s);
                    //Log.d(TAG, s2);

                }
            }).start();
        }

        private void getSignalQuality (final ArrayList<Double> data) {

            tp9Rdy = false;
            tp10Rdy = false;
            fp1Rdy = false;
            fp2Rdy = false;

            if (data.get(Eeg.TP9.ordinal()) <= 1) {
                tp9Rdy = true;
            }
            if (data.get(Eeg.TP10.ordinal()) <= 1) {
                tp10Rdy = true;
            }
            if (data.get(Eeg.FP1.ordinal()) <= 1) {
                fp1Rdy = true;
            }
            if (data.get(Eeg.FP2.ordinal()) <= 1) {
                fp2Rdy = true;
            }
            //Log.d(TAG, String.valueOf(tp9Rdy));

        }
    }

    public boolean getTp9Rdy () {
        return tp9Rdy;
    }
    public boolean getTp10Rdy () {
        return tp10Rdy;
    }
    public boolean getFp1Rdy () {
        return fp1Rdy;
    }
    public boolean getFp2Rdy () {
        return fp2Rdy;
    }
    public boolean checkDataStatus () {
        return alphaDataRdy;
    }
}
