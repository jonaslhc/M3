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

    Button meditationButton;
    Button stroopButton;
    Button overviewButton;

    ShowcaseView showcaseView;
    Target meditation_target, stroop_target, profile_target;
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TextView welcome = (TextView)findViewById(R.id.welcome);
        String welcomeMsg = DatabaseHandler.getHandler().getCurrUser().getName();
        welcome.setText(welcomeMsg);

        overviewButton = (Button) findViewById(R.id.b_profile);
        overviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_to_overview();
            }
        });

        meditationButton = (Button) findViewById(R.id.b_meditation);
        meditationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_calibration();
            }
        });

        stroopButton = (Button) findViewById(R.id.b_stroop);
        stroopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        // Showcaseview only when it's user's first time
        meditation_target = new ViewTarget(R.id.b_meditation, this);
        stroop_target = new ViewTarget(R.id.b_stroop, this);
        profile_target = new ViewTarget(R.id.b_profile, this);
        if (DatabaseHandler.getHandler().getCurrUser().getFirst()) {


            showcaseView = new ShowcaseView.Builder(this)
                    .setTarget(Target.NONE)
                    .setOnClickListener(this)
                    .setContentTitle("Walking Through")
                    .setContentText("This will guide you through the correct usage of our app")
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .build();
            showcaseView.setButtonText("OK");

            meditationButton.setBackground(getResources().getDrawable(R.drawable.grey_button));
            meditationButton.setEnabled(false);
        } else {
            meditationButton.setBackground(getResources().getDrawable(R.drawable.black_button));
            meditationButton.setEnabled(true);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void start_calibration() {
        Intent intent = new Intent(this, CalibrationActivity.class);
        startActivity(intent);
    }

    public void go_to_overview() {
        /*if(DatabaseHandler.getHandler().getLatestMeditation() == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
            builder.setMessage("You have to have at least 1 meditation session before you can see your results!").
                    setTitle("Error");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //empty
                }
            }).create().show();}
        else {*/
            Intent intent = new Intent(this, OverviewActivity.class);
            startActivity(intent);
        //}
    }

    private void start_stroop() {
        Intent intent = new Intent(this, StroopActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch(pos) {
            case 0:
                showcaseView.setShowcase(stroop_target, false);
                showcaseView.setContentTitle("Stroop");
                showcaseView.setContentText("We will start off with a brain game that will be used to evaluate your cognitive performance");
                break;
            case 1:
                showcaseView.setShowcase(meditation_target, false);
                showcaseView.setContentTitle("Meditation");
                showcaseView.setContentText("Later, we will guide you through a meditation session");
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