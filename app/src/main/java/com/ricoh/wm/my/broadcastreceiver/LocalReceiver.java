package com.ricoh.wm.my.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.activity.MainActivity;

/**
 * 本地广播
 */
public class LocalReceiver extends BroadcastReceiver {
    String action = "android.intent.action.LOCAL_BROADCAST";

    public LocalReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

//       if(intent.getAction().equals(action)){
        Toast.makeText(context, "Received Local Broadcast", Toast.LENGTH_SHORT).show();
//         }
    }
}
