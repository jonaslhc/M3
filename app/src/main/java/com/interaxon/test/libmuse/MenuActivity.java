package com.interaxon.test.libmuse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;

public class MenuActivity extends Activity {

    Button meditation;
    Button stroop_button;

    static boolean calibrated = false;
    static boolean usernameSet = false;
    static ProfileData currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (!usernameSet) {
            Intent intent = getIntent();
            String username = intent.getStringExtra(ProfileActivity.EXTRA_MESSAGE);
            currentUser = DatabaseHandler.getHandler().getData(username);

            usernameSet = true;
        }
        TextView welcome = (TextView)findViewById(R.id.welcome);
        welcome.setText(getResources().getString(R.string.welcome)+"\n" + currentUser.getName());



        Button calibration = (Button) findViewById(R.id.b_calibration);
        calibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_calibration();
            }
        });

        meditation = (Button) findViewById(R.id.b_meditation);
        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_meditation();
            }
        });
        // first time menu is created, calibrated should be false
        // after calibration, this value should turn to true for the rest of the session
        if (!calibrated) {
            meditation.setBackgroundColor(getResources().getColor(R.color.Grey));
            meditation.setEnabled(false);
        } else {
            meditation.setBackgroundColor(getResources().getColor(R.color.Black));
            meditation.setEnabled(true);
        }

        stroop_button = (Button) findViewById(R.id.b_stroop);
        stroop_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        start_stroop();
                    }
                }).create().show();
                // 3. Get the AlertDialog from create()
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void start_calibration() {
        Intent intent = new Intent(this, CalibrationActivity.class);
        startActivity(intent);
    }

    public void start_meditation() {
        Intent intent = new Intent(this, MeditationActivity.class);
        startActivity(intent);
    }

    private void start_stroop() {
        Intent intent = new Intent(this, StroopActivity.class);
        startActivity(intent);
    }
}