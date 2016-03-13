package com.interaxon.test.libmuse.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.interaxon.test.libmuse.Museheadband.MuseHandler;
import com.interaxon.test.libmuse.R;

import org.w3c.dom.Text;

public class SignalQualityFragment extends Fragment {

    TextView signalQualitySign;
    TextView leQualitySign;
    TextView reQualitySign;
    TextView lfQualitySign;
    TextView rfQualitySign;

    ProgressBar pgbLe;
    ProgressBar pgbRe;
    ProgressBar pgbLf;
    ProgressBar pgbRf;

    Boolean le_bool;
    Boolean re_bool;
    Boolean lf_bool;
    Boolean rf_bool;

    public SignalQualityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signal_quality, container, false);

        signalQualitySign = (TextView) view.findViewById(R.id.signalQ);
        signalQualitySign.setTextColor(getResources().getColor(R.color.Grey));

        leQualitySign = (TextView) view.findViewById(R.id.le_bool);
        leQualitySign.setText("Bad");
        leQualitySign.setTextColor(getResources().getColor(R.color.Grey));

        reQualitySign = (TextView) view.findViewById(R.id.re_bool);
        reQualitySign.setText("Bad");
        reQualitySign.setTextColor(getResources().getColor(R.color.Grey));

        lfQualitySign = (TextView) view.findViewById(R.id.lf_bool);
        lfQualitySign.setText("Bad");
        lfQualitySign.setTextColor(getResources().getColor(R.color.Grey));

        rfQualitySign = (TextView) view.findViewById(R.id.rf_bool);
        rfQualitySign.setText("Bad");
        rfQualitySign.setTextColor(getResources().getColor(R.color.Grey));

        pgbLe = (ProgressBar) view.findViewById(R.id.pgb_le);
        pgbRe = (ProgressBar) view.findViewById(R.id.pgb_re);
        pgbLf = (ProgressBar) view.findViewById(R.id.pgb_lf);
        pgbRf = (ProgressBar) view.findViewById(R.id.pgb_rf);

        le_bool = false;
        re_bool = false;
        lf_bool = false;
        rf_bool = false;

        monitorQuality();

        return view;
    }

    public void monitorQuality () {
        new Thread(new Runnable() {

            @Override
            public void run() {
                pgbLe.setVisibility(View.VISIBLE);
                pgbRe.setVisibility(View.VISIBLE);
                pgbLf.setVisibility(View.VISIBLE);
                pgbRf.setVisibility(View.VISIBLE);

                while (!(le_bool&&re_bool&&lf_bool&&rf_bool)) {
                    lf_bool = MuseHandler.getHandler().getFp1Rdy();
                    rf_bool = MuseHandler.getHandler().getFp2Rdy();
                    re_bool = MuseHandler.getHandler().getTp10Rdy();
                    le_bool = MuseHandler.getHandler().getTp9Rdy();

                    if (le_bool) {
                        pgbLe.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbLe.setVisibility(View.INVISIBLE);
                                leQualitySign.setTextColor(getResources().getColor(R.color.Blue));
                                leQualitySign.setText("Good");
                            }
                        });
                    } else {
                        pgbLe.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbLe.setVisibility(View.VISIBLE);
                                leQualitySign.setTextColor(getResources().getColor(R.color.Grey));
                            }
                        });
                    }
                    if (re_bool) {
                        pgbRe.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbRe.setVisibility(View.INVISIBLE);
                                reQualitySign.setTextColor(getResources().getColor(R.color.Blue));
                                reQualitySign.setText("Good");

                            }
                        });
                    } else {
                        pgbRe.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbRe.setVisibility(View.VISIBLE);
                                reQualitySign.setTextColor(getResources().getColor(R.color.Grey));
                            }
                        });
                    }
                    if (lf_bool) {
                        pgbLf.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbLf.setVisibility(View.INVISIBLE);
                                lfQualitySign.setTextColor(getResources().getColor(R.color.Blue));
                                lfQualitySign.setText("Good");

                            }
                        });
                    } else {
                        pgbLf.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbLf.setVisibility(View.VISIBLE);
                                lfQualitySign.setTextColor(getResources().getColor(R.color.Grey));
                            }
                        });
                    }
                    if (rf_bool) {
                        pgbRf.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbRf.setVisibility(View.INVISIBLE);
                                rfQualitySign.setTextColor(getResources().getColor(R.color.Blue));
                                rfQualitySign.setText("Good");

                            }
                        });
                    } else{
                        pgbRf.post(new Runnable() {
                            @Override
                            public void run() {
                                pgbRf.setVisibility(View.VISIBLE);
                                rfQualitySign.setTextColor(getResources().getColor(R.color.Grey));
                            }
                        });
                    }

                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                getFragmentManager().beginTransaction().add(R.id.frag_container_med,
                        new CalibrateFragment()).commit();

            }
        }).start();
    }

}
