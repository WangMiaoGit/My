package com.ricoh.wm.my.activity;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.ricoh.wm.my.broadcastreceiver.ForceOffLineReceiver;
import com.ricoh.wm.my.collector.ActivityCollector;

public class BaseActivity extends AppCompatActivity {

    //1AAAAA
    private ForceOffLineReceiver receiver;
    private String action="android.intent.action.FORCE_OFFLINE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("BaseActivity",getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        receiver = new ForceOffLineReceiver();
        registerReceiver(receiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null){
            //注销广播
            unregisterReceiver(receiver);
        }
    }
}
