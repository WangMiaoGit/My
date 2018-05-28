package com.ricoh.wm.my.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ricoh.wm.my.welcome.WelcomeActivity;

/**
 * 开机自启的 广播接收者  静态注册
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Toast.makeText(context, "Boot Complete", Toast.LENGTH_SHORT).show();
        Intent startIntent = new Intent(context, WelcomeActivity.class);
        //开机自启需要设置FLAG_ACTIVITY_NEW_TASK
        startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startIntent);
    }
}
