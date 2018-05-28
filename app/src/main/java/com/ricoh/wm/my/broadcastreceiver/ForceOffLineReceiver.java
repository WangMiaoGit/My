package com.ricoh.wm.my.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.ricoh.wm.my.activity.LoginActivity;
import com.ricoh.wm.my.collector.ActivityCollector;

/**
 * 强制下线的广播  动态注册
 */
public class ForceOffLineReceiver extends BroadcastReceiver {
    public ForceOffLineReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //标题
        builder.setTitle("Warning!!!");
        //消息
        builder.setMessage("您的账号已在别地登录，如不是本人操作，请尽快修改密码");
        //不能取消
        builder.setCancelable(false);
        //按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //销毁所有活动
                ActivityCollector.finishAllActivity();
                //打开重新登录页面
                Intent intent = new Intent(context, LoginActivity.class);
                //在广播中启动活动，需要添加如下代码
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
        AlertDialog alterDialog = builder.create();
        //添加对话框类型：保证在广播中正常弹出
        alterDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        //对话框展示



        builder.show();
    }
}
