package com.ricoh.wm.my.update;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by 2017063001 on 2018/6/28.
 * 下载调度管理，调用UpdateDownlodaRequest
 */
public class UpdateManager {
    private  static UpdateManager manager;

    private ThreadPoolExecutor threadPoolExecutor;//线程池

    private UpdateDownloadRequest updateDownloadRequest;

    private UpdateManager(){
        threadPoolExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
    }

    static {
        manager = new UpdateManager();
    }

    public static  UpdateManager getInstance(){
        return manager;
    }

    public void startDownloads(String downloadUrl ,String localPath,UpdateDownloadListener listener){

        if (updateDownloadRequest != null){
            return;
        }

        checkLocalFilePath(localPath);//检查路径是否合法


        //开始真正下载
        updateDownloadRequest = new UpdateDownloadRequest(downloadUrl,localPath,listener);

        Future<?> future = threadPoolExecutor.submit(updateDownloadRequest);

    }


    //用来检查文件路径是否已经存在。
    private void checkLocalFilePath(String localPath) {

        File dir = new File(localPath.substring(0,localPath.lastIndexOf("/")+1));//文件夹位置
        if(!dir.exists()){//文件夹不存在
            dir.mkdir();    //就创建
        }

        File file = new File(localPath);

        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
