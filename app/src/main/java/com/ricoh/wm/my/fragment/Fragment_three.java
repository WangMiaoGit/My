package com.ricoh.wm.my.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ricoh.wm.my.R;

import java.util.Timer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_three extends Fragment {


    @Bind(R.id.bt_shock)
    Button btShock;

    //震动器对象
    private Vibrator vibrator;

    boolean shock = true;

    public Fragment_three() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        ButterKnife.bind(this, view);


        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.bt_shock)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.bt_shock:
//                vibrator.vibrate(2000);
                if (shock){
                    vibrator.vibrate(new long[]{0,300,50,100,50,100},0);
                    shock=false;
                }else {
                    vibrator.cancel();
                    shock=true;
                }



//                try {
//                    Thread.sleep(5000);
//                    vibrator.cancel();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                break;
        }

    }
}
