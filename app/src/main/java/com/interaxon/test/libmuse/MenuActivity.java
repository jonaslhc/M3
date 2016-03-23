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

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.Fragments.CalibrateFragment;

public class MenuActivity extends Activity implements View.OnClickListener{

    Button meditation;
    Button stroop_button;

    static boolean calibrated = false;
    static boolean usernameSet = false;

    public static String USERNAME;
    ShowcaseView showcaseView;
    Target meditation_target, stroop_target, profile_target;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (!usernameSet) {
            Intent intent = getIntent();
            String username = intent.getStringExtra(ProfileActivity.EXTRA_MESSAGE);
            USERNAME = username;
            DatabaseHandler.getHandler().updateCurrUser(username);
            usernameSet = true;

        } else if (!calibrated) {
            Intent intent = getIntent();
            String calibrate_info = intent.getStringExtra(CalibrateFragment.EXTRA_MESSAGE);

            // this line produces a crash, null object reference
            if (calibrate_info.equals("calibrated")) calibrated = true;
        }

        // Set up Showcaseview
        meditation_target = new ViewTarget(R.id.b_meditation, this);
        stroop_target = new ViewTarget(R.id.b_stroop, this);
        profile_target = new ViewTarget(R.id.b_profile, this);

        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setOnClickListener(this)
                .setContentTitle("Walk Through")
                .setContentText("This will guide you through the correct usage of our app")
                .setStyle(R.style.CustomShowcaseTheme2)
                .build();
        showcaseView.setButtonText("OK");



        TextView welcome = (TextView)findViewById(R.id.welcome);
        String welcomeMsg = getResources().getString(R.string.welcome) +
                " " +DatabaseHandler.getHandler().getCurrUser().getName();
        welcome.setText(welcomeMsg);

        //Button calibration = (Button) findViewById(R.id.b_meditation);
        //calibration.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        start_calibration();
        //    }
        //});

        meditation = (Button) findViewById(R.id.b_meditation);
        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_calibration();
            }
        });
        // first time menu is created, calibrated should be false
        // after calibration, this value should turn to true for the rest of the session
        /*if (!calibrated) {
            meditation.setBackgroundColor(getResources().getColor(R.color.Grey));
            meditation.setEnabled(false);
        } else {
            meditation.setBackgroundColor(getResources().getColor(R.color.Black));
            meditation.setEnabled(true);
        }*/

        stroop_button = (Button) findViewById(R.id.b_stroop);
        stroop_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.stroop_message).setTitle(R.string.dialog_title);
                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        start_stroop();
                    }
                }).create().show();
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

    @Override
    public void onClick(View v) {
        switch(pos) {
            case 0:
                showcaseView.setShowcase(meditation_target, false);
                showcaseView.setContentTitle("Meditation");
                showcaseView.setContentText("We will guide you through a meditation session");
                break;
            case 1:
                showcaseView.setShowcase(stroop_target, false);
                showcaseView.setContentTitle("Stroop");
                showcaseView.setContentText("We will begin a brain game that will be used to evaluate your cognitive performance");
                break;
            case 2:
                showcaseView.setShowcase(profile_target, false);
                showcaseView.setContentTitle("Profile");
                showcaseView.setContentText("This will take you to your profile page, where you have access to your records and information");
                break;
            case 3:
                showcaseView.hide();
                break;
        }
        pos++;

    }

}