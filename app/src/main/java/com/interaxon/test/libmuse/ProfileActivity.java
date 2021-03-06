package com.interaxon.test.libmuse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.interaxon.test.libmuse.Data.DatabaseHandler;
import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.Fragments.AddMemberFragment;
import com.interaxon.test.libmuse.Fragments.EnterMemberFragment;
import com.interaxon.test.libmuse.Fragments.MainPageFragment;

public class ProfileActivity extends FragmentActivity implements EnterMemberFragment.CheckMemberListener, AddMemberFragment.NewMemberListener {

    Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mContext = this;

        DatabaseHandler.initHandler(mContext);
        //DatabaseHandler.getHandler().deleteDatabase();
        //DatabaseHandler.initHandler(mContext);

        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,
                new MainPageFragment()).addToBackStack(null).commit();
    }

    @Override
    protected void onResume (){
        super.onResume();

        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,
                new MainPageFragment()).addToBackStack(null).commit();
    }

    public void StartMenuActivity(ProfileData member_info) {
        DatabaseHandler.getHandler().setCurrUser(member_info);
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    //////////////////////////
    // FRAGMENT LISTENERS
    //////////////////////////

    public void NewMemberListener (ProfileData member_info) {
        DatabaseHandler.getHandler().addUser(member_info);
        StartMenuActivity(member_info);
    }

    public void CheckMemberListener (ProfileData member_info) {
        if (DatabaseHandler.getHandler().checkUser(member_info)) {
            StartMenuActivity(member_info);
        } else {
            Toast toast = Toast.makeText(mContext, "Wrong Password.", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



}
