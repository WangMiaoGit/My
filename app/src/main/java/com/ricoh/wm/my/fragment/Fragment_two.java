package com.ricoh.wm.my.fragment;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.broadcastreceiver.LocalReceiver;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_two extends Fragment {


    private static final String TAG = "Fragment_two";
    @Bind(R.id.button_local)
    Button buttonLocal;
    @Bind(R.id.button_not)
    Button buttonNot;
    @Bind(R.id.button_zhiwen)
    Button buttonZhiwen;
    @Bind(R.id.button_1)
    Button button1;
    /**
     * 意图过滤器
     */
    private IntentFilter intentFilter;
    /**
     * 本地广播管理器
     */
    private LocalBroadcastManager localBroadcastManager;
    /**
     * 本地广播接收者
     */
    private LocalReceiver localReceiver;

    /**
     * 通知的管理对象
     */
    NotificationManager manager;
    /**
     * 通知的id
     */
    int notification_id;

    //定义notification实用的ID
    private static final int NO_3 = 0x3;


    /**
     * 指纹的对话框
     */
    private AlertDialog.Builder customizeDialog;
    private AlertDialog alertDialog;


    private String action="android.intent.action.FORCE_OFFLINE";
    public Fragment_two() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);

        /**
         * 动态注册本地广播
         */
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext().getApplicationContext());//获取实例

        intentFilter = new IntentFilter();

        intentFilter.addAction("android.intent.action.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);//注册本地广播监听器

        manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        //指纹弹出的
        customizeDialog =
                new AlertDialog.Builder(getContext());


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        localBroadcastManager.unregisterReceiver(localReceiver);//注销广播监听
    }

    @OnClick({R.id.button_local, R.id.button_not, R.id.button_zhiwen,R.id.button_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_local:
                /**
                 * 发送本地广播，给本地广播接收者接收
                 */
                Intent intent = new Intent("android.intent.action.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);//发送广播
                break;
            case R.id.button_not:
                Notification.Builder builder = new Notification.Builder(getContext());
                //不设置图标就不会有通知的显示
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setTicker("World");
                builder.setWhen(System.currentTimeMillis());
                builder.setContentTitle("标题栏");
                builder.setContentText("通知需要显示的内容");


//                Intent intent = new Intent(MainActivity.this, Activity.class);
//                PendingIntent ma = PendingIntent.getActivity(MainActivity.this,0,intent,0);
//                builder.setContentIntent(ma);//设置点击过后跳转的activity


                builder.setDefaults(Notification.DEFAULT_SOUND);//设置声音
                builder.setDefaults(Notification.DEFAULT_LIGHTS);//设置指示灯
                builder.setDefaults(Notification.DEFAULT_VIBRATE);//设置震动
                builder.setDefaults(Notification.DEFAULT_ALL);//设置全部

                Notification notification = builder.build();//4.1以上用.build();
                /**
                 * FLAG_AUTO_CANCEL
                 当通知被用户点击之后会自动被清除(cancel)

                 FLAG_INSISTENT
                 在用户响应之前会一直重复提醒音和震动,(如果在default那里开启的话)。

                 FLAG_ONGOING_EVENT
                 表示正在运行的事件,例如下载进度。不能清除。

                 FLAG_NO_CLEAR
                 通知栏点击“清除”按钮时，该通知将不会被清除。或者没有清除的按钮

                 FLAG_FOREGROUND_SERVICE
                 表示当前服务是前台服务，简单通知。默认
                 */
                notification.flags |= Notification.FLAG_AUTO_CANCEL;// 点击通知的时候cancel掉
                manager.notify(notification_id, notification);


                //带进度条的
                /*final NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
                         builder.setSmallIcon(R.mipmap.ic_launcher);
                         builder.setContentTitle("下载");
                         builder.setContentText("正在下载");
//                builder.setDefaults(Notification.DEFAULT_ALL);
                         final NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                         manager.notify(NO_3, builder.build());
                         builder.setProgress(100,0,false);
                         //下载以及安装线程模拟
                         new Thread(new Runnable() {
                             @Override
                             public void run() {
                                     for(int i=0;i<100;i++){
                                             builder.setProgress(100,i,false);
                                             manager.notify(NO_3,builder.build());
                                             //下载进度提示
                                             builder.setContentText("下载"+i+"%");
                                             try {
                                                     Thread.sleep(50);//演示休眠50毫秒
                                             } catch (InterruptedException e) {
                                                     e.printStackTrace();
                                             }
                                         }
                                     //下载完成后更改标题以及提示信息
                                     builder.setContentTitle("开始安装");
                                     builder.setContentText("安装中...");
                                    //设置进度为不确定，用于模拟安装
                                   builder.setProgress(0,0,true);
                                   manager.notify(NO_3,builder.build());
                     //                manager.cancel(NO_3);//设置关闭通知栏
                               }
                       }).start();*/
                break;
            case R.id.button_zhiwen:
                Log.d("btn", "开始识别");

//                move(buttonZhiwen);
                checkFingerPrint();
                break;
            case R.id.button_1:
                //发送下线广播
                /**
                 * 异地登录强制下线实现：每次登录上传一个token然后从服务器返回一个信号，检查token，如果不同，强制退出
                 *
                 */
                Intent intent1 = new Intent(action);
                getContext().sendBroadcast(intent1);
                break;
            default:
        }
    }

    /**
     * 指纹识别
     */
    private void checkFingerPrint() {
        FingerprintManagerCompat.from(getContext()).authenticate(null, 0, null, new MyCallBack(), null);
        showCustomizeDialog(customizeDialog);
    }

    /**
     * 指纹识别的回调
     */
    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";

        @Override
        public void onAuthenticationFailed() {
            Log.d(TAG, "指纹错误");
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Log.d(TAG, "指纹正确");
            alertDialog.dismiss();

        }
    }

    /**
     * 指纹弹出的对话框
     */
    public void showCustomizeDialog(AlertDialog.Builder customizeDialog) {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
//        AlertDialog.Builder customizeDialog =
//                new AlertDialog.Builder(getContext());
        final View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_customize, null);//自定义的对话框
        customizeDialog.setView(dialogView);
        TextView viewById = (TextView) dialogView.findViewById(R.id.text_zhiwen);

        move(viewById);

        alertDialog = customizeDialog.show();

    }


    /**
     * 平移动画
     */
    public void move(View view) {
        //平移动画
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -50, 50);
        //匀速
        translateAnimation.setInterpolator(new LinearInterpolator());
        //持续时间
        translateAnimation.setDuration(2000);
        //留在最后的地方
        translateAnimation.setFillAfter(true);
        //重复循环动画
        translateAnimation.setAnimationListener(new ReStartAnimationListener());

        //动画集
        AnimationSet animationSet = new AnimationSet(true);
        //动画集中加入动画
        animationSet.addAnimation(translateAnimation);

        //开始动画
        view.startAnimation(translateAnimation);
    }

    /**
     * 重复启动动画
     */
    private class ReStartAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            // TODO Auto-generated method stub
            animation.reset();
            animation.setAnimationListener(new ReStartAnimationListener());
            animation.start();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub

        }

    }
}
