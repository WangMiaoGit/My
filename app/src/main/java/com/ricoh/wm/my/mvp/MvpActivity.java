package com.ricoh.wm.my.mvp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.activity.MainActivity;
import com.ricoh.wm.my.model.User;
import com.ricoh.wm.my.mvp.model.LoginModel;
import com.ricoh.wm.my.mvp.presenter.LoginPresenter;
import com.ricoh.wm.my.mvp.view.LoginView;

/**
 * @author wangmiao
 * mvp 模式中 view层的实现类
 */
public class MvpActivity extends AppCompatActivity implements LoginView {

    private EditText et_userName;
    private EditText et_password;
    private ProgressBar progressBar;
    /**
     * presenter 中的loginPresenter
     */
    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        initView();
        loginPresenter = new LoginPresenter(this);
    }

    private void initView() {
        et_userName = (EditText) findViewById(R.id.main_et_username);
        et_password = (EditText) findViewById(R.id.main_et_password);
        progressBar = (ProgressBar) findViewById(R.id.main_progressBar);
    }

    /**点击登录
     *
     * @param view
     */
    public void LoginClick(View view) {
        //LoginPresenter
        loginPresenter.login();
    }

    /**点击清除
     *
     * @param view
     */
    public void ClearClick(View view) {
        //LoginPresenter
        loginPresenter.clear();
    }

    @Override
    public String getUsername() {
        //LoginView
        return et_userName.getText().toString();
    }

    @Override
    public String getPassword() {
        //LoginView
        return et_password.getText().toString();
    }

    @Override
    public Context context() {
        //LoginView
        return this;
    }

    @Override
    public void showLoading() {
        //LoginView
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        //LoginView
        progressBar.setVisibility(View.GONE);
    }

    /**
     * LoginView  中的具体实现
     * @param user
     */
    @Override
    public void showSuccessMsg(User user) {
        //LoginView
        MainActivity.actionStart(this.context(), this.getUsername());
        this.finish();
        Toast.makeText(MvpActivity.this, "User " + user.getNum() + " Login Sccess!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailedMsg(String s) {
        //LoginView
        Toast.makeText(MvpActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearEditText() {
        et_userName.setText("");
        et_password.setText("");
    }
}
