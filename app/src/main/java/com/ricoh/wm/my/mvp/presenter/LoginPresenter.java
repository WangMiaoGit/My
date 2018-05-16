package com.ricoh.wm.my.mvp.presenter;

import android.os.Handler;

import com.ricoh.wm.my.activity.MainActivity;
import com.ricoh.wm.my.model.User;
import com.ricoh.wm.my.mvp.model.LoginModel;
import com.ricoh.wm.my.mvp.model.LoginModelImpl;
import com.ricoh.wm.my.mvp.view.LoginView;



/**
 * @author
 * Created by 2017063001 on 2018/3/10.
 */
public class LoginPresenter {
    /**
     * mvp 中 view 层的实现 loginView
     */
    private LoginView loginView;
    /**
     * mvp 中 model 层的实现 loginModel
     */
    private LoginModel loginModel;
    private Handler mHandler;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModelImpl();
        mHandler = new Handler();
    }


    public void login() {
        loginView.showLoading();
        loginModel.login(loginView.getUsername(), loginView.getPassword(),loginView.context(), new LoginModel.OnLoginListener() {
            @Override
            public void loginSuccess(final User user) {
                //模拟登录成功后，返回信息到Activity,吐出成功信息
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showSuccessMsg(user);
                        loginView.hideLoading();
                    }
                });
            }

            @Override
            public void loginFailed(final String s) {
                //模拟登录失败后，返回信息，吐出错误信息
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginView.showFailedMsg(s);
                        loginView.hideLoading();
                    }
                });
            }
        });
    }

    public void clear(){
        loginView.clearEditText();
    }
}
