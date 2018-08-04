package com.ricoh.wm.my.interfaces;

import android.nfc.Tag;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by 2017063001 on 2018/6/20.
 */
public class JSInterface {

    private static final String TAG="JSInterface";


    private JSBridge jsBridge;//声明接口对象，让JSInterface可以拿到接口并调用接口方法
    public JSInterface(JSBridge jsBridge){
        this.jsBridge=jsBridge;
    }

    //只有这个注解才能让webview识别
    /**
     * 方法不在主线程中执行
     * @param value
     */
    @JavascriptInterface
    public void setValue(String value){
        Log.d(TAG,"value="+value);

        jsBridge.setTextViewValue(value);//执行接口的方法，WebViewActivity实现了接口，相当于调用WebViewActivity中接口方法的实现
    }
}
