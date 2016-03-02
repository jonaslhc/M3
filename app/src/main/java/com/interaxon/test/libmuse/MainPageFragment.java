package com.interaxon.test.libmuse;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainPageFragment extends Fragment {
    
    public MainPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main_page, container, false);

        Button new_member = (Button) view.findViewById(R.id.b_new_member);
        new_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_new_member();
            }
        });

        Button old_member = (Button) view.findViewById(R.id.b_old_member);
        old_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find_existing_member();
            }
        });


        return view;

    }


    public void add_new_member() {

        getFragmentManager().beginTransaction().replace(R.id.frag_container,
                new AddMemberFragment()).addToBackStack(null).commit();
    }

    public void find_existing_member() {

        getFragmentManager().beginTransaction().replace(R.id.frag_container,
                new EnterMemberFragment()).addToBackStack(null).commit();

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }

}

