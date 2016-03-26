package com.interaxon.test.libmuse.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.R;

public class AddMemberFragment extends Fragment {

    EditText name, age, username, password, password2;

    public AddMemberFragment() {
        // Required empty public constructor
    }

    private NewMemberListener mListener;

    public interface NewMemberListener {
        void NewMemberListener(ProfileData newMember);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);

        name = (EditText)view.findViewById(R.id.enter_name);
        age = (EditText) view.findViewById(R.id.enter_age);
        username = (EditText)view.findViewById(R.id.enter_username);
        password = (EditText)view.findViewById(R.id.enter_password);
        password2 = (EditText)view.findViewById(R.id.enter_password2);

        Button old_user = (Button)view.findViewById(R.id.b_back_old_member);
        old_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterMember();
            }
        });

        Button add_user = (Button)view.findViewById(R.id.b_add_member);
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().equals("") || age.getText().toString().equals("")  ||
                        username.getText().toString().equals("")  || password.getText().toString().equals("")  ||
                        password2.getText().toString().equals(""))  {
                    Toast.makeText(getActivity(), "Please complete the form.",Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(password2.getText().toString())) {
                    Log.d("password check", password.getText().toString());
                    Log.d("passwrod check 2", password2.getText().toString());
                    Toast.makeText(getActivity(), "Your passwords do not match.",Toast.LENGTH_SHORT).show();
                } else {
                    addMember();
                }
            }
        });

        return view;
    }

    public void addMember() {

        ProfileData newMember = new ProfileData(username.getText().toString(), password.getText().toString(),
                name.getText().toString(), Integer.parseInt(age.getText().toString()), null);

        mListener.NewMemberListener(newMember);
        this.getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    public void enterMember () {
        getFragmentManager().beginTransaction().replace(R.id.frag_container,
                new EnterMemberFragment()).addToBackStack(null).commit();

    }

    //@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (NewMemberListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAddListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
