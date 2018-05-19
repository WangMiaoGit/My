package com.ricoh.wm.my.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.ricoh.wm.my.R;
import com.ricoh.wm.my.broadcastreceiver.ForceOffLineReceiver;
import com.ricoh.wm.my.collector.ActivityCollector;

public class BaseActivity extends AppCompatActivity {

    //自定义广播接收者  ---------------------强制退出
    private ForceOffLineReceiver receiver;
    //广播的action  ---------------------强制退出
    private String action="android.intent.action.FORCE_OFFLINE";

    //网络改变的广播
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

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


    //注册广播
    @Override
    protected void onResume() {
        super.onResume();
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(action);
        receiver = new ForceOffLineReceiver();
        registerReceiver(receiver,intentFilter);

        initBroadCast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null){
            //注销广播
            unregisterReceiver(receiver);

        }
        if (networkChangeReceiver!=null){
            unregisterReceiver(networkChangeReceiver);
        }
    }

    //初始化广播接受者
    private void initBroadCast() {
        //构建意图过滤器
        intentFilter = new IntentFilter();
        //添加要接收的广播
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        //注册广播传入一个BroadcastReceiver子类实现onReceive方法，还有意图过滤器
        registerReceiver(networkChangeReceiver, intentFilter);
    }


    /**
     * 网络切换的广播接收者
     */
    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {


                Toast.makeText(context, "Network is available", Toast.LENGTH_SHORT).show();
            } else {

                Snackbar snackBar = Snackbar.make(getWindow().getDecorView(), "网络连接断开！！！", Snackbar.LENGTH_LONG);
                //设置SnackBar背景颜色
                snackBar.getView().setBackgroundResource(R.color.red);
                //设置按钮文字颜色
                snackBar.setActionTextColor(Color.WHITE);
                snackBar.show();
//                Toast.makeText(context, "Network is unavailable", Toast.LENGTH_SHORT).show();

            }

        }
    }
}
