package com.ricoh.wm.my.update;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by 2017063001 on 2018/6/28.
 * 文件下载线程通信
 */
public class UpdateDownloadRequest implements Runnable {
    private String downloadUrl;//下载路径
    private String localFilePath;//文件保存路径
    private UpdateDownloadListener downloadListener;//下载的回调
    private boolean isDownloading = false;//下载标志位
    private long currentLength;//文件长度

    private DownloadResponseHandler downloadResponseHandler;

    public UpdateDownloadRequest(String downloadUrl, String localFilePath, UpdateDownloadListener downloadListener) {
        this.downloadUrl = downloadUrl;
        this.localFilePath = localFilePath;
        this.downloadListener = downloadListener;

        this.isDownloading =true;
        this.downloadResponseHandler = new DownloadResponseHandler();
    }

    @Override
    public void run() {

        try {
            makeRequest();//建立连接
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //真正建立连接
    private void makeRequest()throws IOException,InterruptedException{
        if (!Thread.currentThread().isInterrupted()){//线程为被打断
            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setRequestProperty("Connection","Keep-Alive");//连接建立方式---Keep-Alive
                connection.connect();//阻塞当前线程

                currentLength = connection.getContentLength();//文件长度
                if (!Thread.currentThread().isInterrupted()){
                    //真正完成文件的下载
                    downloadResponseHandler.sendResponseMessage(connection.getInputStream());
                }
            }catch (IOException e){
                throw e;
            }

        }
    }

    /**
     * 格式化数字
     */
    private String getTwoPointFloadSrt(float value){
        DecimalFormat fnum = new DecimalFormat("0.00");
        return fnum.format(value);
    }

    /**
     * 包含下载过程中所有可能出现的异常情况  枚举类型
     */
    public enum  FailureCode{
        UnknownHost,Socket,SocketTimeout,ConnectTimeout,
        IO,HttpResopnse,JSON,Interrupted
    }











    /**
     * 用来真正下载文件，并发送消息和回调的接口-----------------内部类
     */
    public class DownloadResponseHandler {
        protected static final int SUCCESS_MESSAGE = 0;
        protected static final int FAILURE_MESSAGE = 1;
        protected static final int START_MESSAGE = 2;
        protected static final int FINISH_MESSAGE = 3;
        protected static final int NETWORK_OFF = 4;
        private static final int PROGRESS_CHANGED=5;


        private int mCompleteSize=0;
        private int progress =0;


        private Handler mhandler;//真正的完成线程通信

        public DownloadResponseHandler(){
            mhandler = new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    handleSelfMessage(msg);
                }
            };
        }

        /**
         * 发送不同的消息对象
         */
        protected void sendFinishMessage(){
            sendMessage(obtainMessage(FINISH_MESSAGE,null));
        }

        /**
         * 更新进度 notification
         * @param progress
         */
        protected void sendProgressChangedMessage(int progress){
            sendMessage(obtainMessage(PROGRESS_CHANGED,new Object[]{progress}));
        }

        /**
         * 下载失败的消息
         * @param failureCode
         */
        protected void sendFailureMessage(FailureCode failureCode){
            sendMessage(obtainMessage(FAILURE_MESSAGE,new Object[]{failureCode}));
        }


        /**
         * 发送消息
         * @param msg
         */
        protected void sendMessage(Message msg){
           if (mhandler!=null){
               mhandler.sendMessage(msg);
           }else {
               handleSelfMessage(msg);
           }
        }



        /**
         * 获取一个消息对象  handle
         * @param responseMessage 请求
         * @param response 响应
         * @return
         */
        protected Message obtainMessage(int responseMessage,Object response){
            Message message= null;
            if (mhandler!=null){
                message = mhandler.obtainMessage(responseMessage,response);
            }else {
                message = Message.obtain();
                message.what = responseMessage;
                message.obj = response;
            }

            return message;
        }

        private void handleSelfMessage(Message msg) {
            Object[] response;

            switch (msg.what){
                case FAILURE_MESSAGE:
                    response = (Object[]) msg.obj;
                    handleFailureMessage((FailureCode) response[0]);
                    break;
                case PROGRESS_CHANGED:
                    response = (Object[]) msg.obj;
                   handleProgressChangedMessage(((Integer) response[0]).intValue());
                    break;
                case FINISH_MESSAGE:
                    onFinish();
                    break;
            }
        }


        private void handleFailureMessage(FailureCode failureCode) {
            onFailure(failureCode);
        }
        /**
         * 消息处理逻辑       接口回调
         * @param progress
         */
        private void handleProgressChangedMessage(int progress) {
            downloadListener.onProgressChanged(progress,"");
        }
        private void onFinish() {
            downloadListener.onFinished(mCompleteSize,"");
        }
        private void onFailure(FailureCode failureCode) {
            downloadListener.onFailure();
        }



        /**
         * 文件   下载方法，发送各种类型的事件
         * @param is
         */
        void sendResponseMessage(InputStream is){
            RandomAccessFile randomAccessFile = null;
            mCompleteSize = 0;//完成的大小

            try {
                byte[] buffer = new byte[1024];
                int length = -1;
                int limit = 0;
                randomAccessFile = new RandomAccessFile(localFilePath,"rwd");
                while((length = is.read(buffer) )!=-1){
                    if (isDownloading){
                        randomAccessFile.write(buffer,0,length);//文件写入本地

                        mCompleteSize+=length;//下载长度累计

                        if (mCompleteSize< currentLength){//下载进度
                            progress = (int) Float.parseFloat(
                                    getTwoPointFloadSrt(mCompleteSize/currentLength));
                            if (limit/30 == 0 || progress<=100){
                                //为了限制notification的更新频率
                                sendProgressChangedMessage(progress);
                            }

                            limit++;
                        }
                    }
                }

                sendFinishMessage();//下载完成

            }catch (Exception e){
                sendFailureMessage(FailureCode.IO);
            }finally {

                try {

                    if(is!=null){
                        is.close();
                    }
                    if (randomAccessFile!=null){
                        randomAccessFile.close();
                    }
                }catch (IOException e){
                    sendFailureMessage(FailureCode.IO);//下载失败消息
                }
            }
        }
    }
}
