package com.interaxon.test.libmuse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;

/**
 * Created by st924507 on 2016-03-14.
 */
public class MyResults extends Activity {

    double last_accuracy, last_reaction_time;
    static ProfileData profileData;
    TextView lastAccuracy, lastReactionTime;
    DatabaseHandler databaseHandler;
    String TAG = MyResults.class.getSimpleName();
    MenuActivity menuActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stroop_result_layout);
        menuActivity = new MenuActivity();
        initView();
        showResult();
    }

    private void initView() {
        lastAccuracy = (TextView) findViewById(R.id.last_accuracy);
        lastReactionTime = (TextView) findViewById(R.id.last_reaction_time);
    }

    private void showResult() {
        //Intent intent = getIntent();
        //String username = intent.getStringExtra(ProfileActivity.EXTRA_MESSAGE);

        Log.e(TAG, "hello" + menuActivity.getMyName());

        profileData = databaseHandler.getHandler().getData(menuActivity.getMyName());

        last_accuracy = profileData.getAccuracy();
        last_reaction_time = profileData.getReaction_time();

        lastAccuracy.setText(String.format("Accuracy: %6.2f", last_accuracy));
        lastReactionTime.setText(String.format("Reaction Time: %6.2f", last_reaction_time));


    }

}
