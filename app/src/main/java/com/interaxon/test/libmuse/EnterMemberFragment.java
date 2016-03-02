package com.interaxon.test.libmuse;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class EnterMemberFragment extends Fragment {


    public EnterMemberFragment() {
        // Required empty public constructor
    }

    private CheckMemberListener mListener;

    public interface CheckMemberListener {
        void CheckMemberListener(ProfileData member);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enter_member, container, false);

        final EditText username = (EditText)view.findViewById(R.id.enter_existing_username);
        final EditText password = (EditText)view.findViewById(R.id.enter_existing_password);


        Button check_user = (Button)view.findViewById(R.id.b_check_member);
        check_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileData user = new ProfileData(username.getText().toString(), password.getText().toString(), "");
                checkMember(user);
            }
        });

        Button new_user = (Button)view.findViewById(R.id.b_back_new_member);
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember();
            }
        });
        return view;
    }

    public void checkMember (ProfileData user) {
        mListener.CheckMemberListener(user);
    }

    public void addMember () {
        getFragmentManager().beginTransaction().replace(R.id.frag_container,
                new AddMemberFragment()).addToBackStack(null).commit();
    }

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (CheckMemberListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddMemberListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
