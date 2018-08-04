package com.ricoh.wm.my.update;

/**
 * Created by 2017063001 on 2018/6/28.
 * 事件的监听回调
 */
public interface UpdateDownloadListener {
    /**
     * 下载请求开始回调
     */
     void onStarted();

    /**
     * 进度更新回调
     */
     void onProgressChanged(int progress, String downloadUrl);
    /**
     * 下载完成回调
     */
     void onFinished(int completeSize,String downloadUrl);
    /**
     * 下载失败的回调
     */
     void onFailure();
}
