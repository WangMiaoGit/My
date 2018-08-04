package com.ricoh.wm.my.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.ricoh.wm.my.R;

import java.io.File;

/**
 * Created by 2017063001 on 2018/6/28.
 *  app更新下载后台的服务
 */
public class UpdateService extends Service{


    private String apkURL;//apk下载路径
    private String filePath;
    private NotificationManager notificationManager;
    private Notification mNotification;
    @Override
    public void onCreate(){

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        filePath = Environment.getExternalStorageDirectory()+"/wang/test.apk";      //下载保存本地// 的路径

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null){
            notifyUser(getString(R.string.update_download_failed),
                    getString(R.string.update_download_failed_msg),0);

            stopSelf();
        }

        /**
         * 从LoginActivity  获取的 路径
         */
        apkURL = intent.getStringExtra("apkUrl");//下载路径

        notifyUser(getString(R.string.update_download_start),
                getString(R.string.update_download_start),0);//通知用户


        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 更新
     */
    private void startDownload() {
        UpdateManager.getInstance().startDownloads(apkURL,filePath
                ,new UpdateDownloadListener(){
                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onProgressChanged(int progress, String downloadUrl) {

                        notifyUser(getString(R.string.update_download_processing),
                                getString(R.string.update_download_processing),progress);
                    }

                    @Override
                    public void onFinished(int completeSize, String downloadUrl) {

                        notifyUser(getString(R.string.update_download_finish),
                                getString(R.string.update_download_finish),100);

                        stopSelf();
                    }

                    @Override
                    public void onFailure() {
                        notifyUser(getString(R.string.update_download_failed),
                                getString(R.string.update_download_failed_msg),0);

                        stopSelf();
                    }
        });
    }

    //更新我们的notification告知用户当前下载的进度
    private void notifyUser(String result, String reason,int progress) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name));

        if(progress > 0 && progress<100){
            builder.setProgress(100,progress,false);//显示进度   false 有具体进度
        }else {
            builder.setProgress(0,0,false);//隐藏
        }

        builder.setAutoCancel(true);//可以自动清除
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker(result);

        /**
         * 下载完成  就更新apk
         */
        builder.setContentIntent(progress >=100 ? getContentIntent()//Notification点检的意图
        : PendingIntent.getActivity(this,0,new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT));

        mNotification = builder.build();//创建Notification
        notificationManager.notify(0,mNotification);//发出通知


    }

    /**
     * 安装APK
     * @return
     */
    private PendingIntent getContentIntent() {

        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent .FLAG_ACTIVITY_CLEAR_TASK);
        //调用系统的安装程序
        intent.setDataAndType(Uri.parse("file://"+apkFile.getAbsolutePath()),
                "application/vnd.android.package-archive");

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
