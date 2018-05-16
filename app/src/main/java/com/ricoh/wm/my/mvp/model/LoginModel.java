package com.ricoh.wm.my.mvp.model;

import android.content.Context;

import com.ricoh.wm.my.model.User;

/**
 * @author wangmiao
 * Created by 2017063001 on 2018/3/10.
 * mvp 模式中的 model层的接口
 */
public interface LoginModel {
    void login(String userName, String passWord, Context context,OnLoginListener onLoginListener);

    interface OnLoginListener{
        void loginSuccess(User user);
        void loginFailed(String s);
    }
}
