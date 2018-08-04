package com.ricoh.wm.my.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.interfaces.JSBridge;
import com.ricoh.wm.my.interfaces.JSInterface;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 2017063001 on 2018/6/20.
 */
public class WebViewActivity extends BaseActivity implements JSBridge {


    @Bind(R.id.webview)
    WebView mwebview;
    @Bind(R.id.tv_result)
    TextView mtvResult;
    @Bind(R.id.edittext)
    EditText edittext;
    @Bind(R.id.button)
    Button button;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acyivity_webview);
        ButterKnife.bind(this);

        //运行webviwe加载js代码
        mwebview.getSettings().setJavaScriptEnabled(true);
        //webView添加JS接口
        mwebview.addJavascriptInterface(new JSInterface(WebViewActivity.this), "mLuncher");

        //直接加载.html文件
        mwebview.loadUrl("file:///android_asset/index.html");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mwebview.setWebContentsDebuggingEnabled(true);
        }
        mHandler = new Handler();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edittext.getText().toString();
                mwebview.loadUrl("javascript:if(window.remote){window.remote('"+str+"')}");
            }
        });
    }

    @Override
    public void setTextViewValue(final String s) {//接口中的方法的实现
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mtvResult.setText(s);
            }
        });
    }
}
