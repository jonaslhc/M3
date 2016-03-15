package com.interaxon.test.libmuse.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.interaxon.test.libmuse.Data.ProfileData;
import com.interaxon.test.libmuse.R;

public class AddMemberFragment extends Fragment {

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

        final EditText name = (EditText)view.findViewById(R.id.enter_name);
        final EditText username = (EditText)view.findViewById(R.id.enter_username);
        final EditText password = (EditText)view.findViewById(R.id.enter_password);
        final EditText password2 = (EditText)view.findViewById(R.id.enter_password2);


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
                addMember (new ProfileData(username.getText().toString(),
                        password.getText().toString(), name.getText().toString(), 0.0, 0.0, null));
            }
        });

        return view;
    }

    public void addMember(ProfileData newMember) {
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
