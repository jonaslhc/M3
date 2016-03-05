package com.interaxon.test.libmuse;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MenuActivity extends Activity {

    boolean calibrated;
    Button meditation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String username = intent.getStringExtra(ProfileActivity.EXTRA_MESSAGE);

        ProfileData currentUser = DatabaseHandler.getHandler().getData(username);

        TextView welcome = (TextView)findViewById(R.id.welcome);
        welcome.setText(getResources().getString(R.string.welcome)+"\n" + currentUser.getName());

        // first time menu is created, calibrated should be false
        // after calibration, this value should turn to true for the rest of the session
        calibrated = false;

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (calibrated == false) {
            meditation.setBackgroundColor(getResources().getColor(R.color.Grey));
            meditation.setEnabled(false);
        } else {
            meditation.setBackgroundColor(getResources().getColor(R.color.Black));
            meditation.setEnabled(true);
        }
    }

    public void start_calibration() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        calibrated = true;
    }

    public void start_meditation() {
        Intent intent = new Intent(this, MeditationActivity.class);
        startActivity(intent);
    }


}