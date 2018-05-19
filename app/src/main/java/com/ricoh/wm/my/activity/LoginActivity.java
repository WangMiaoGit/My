package com.ricoh.wm.my.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.interfaces.MyService;
import com.ricoh.wm.my.model.User;
import com.ricoh.wm.my.utils.EncrypAESUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {


    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etPassword)
    EditText etPassword;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.checkBox)
    CheckBox checkBox;


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //获取sharedPreferences对象
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //获取sharedPreferences.edit的对象
        editor = sharedPreferences.edit();

        //获取sharedPreferences中  是否记住密码的字段，如果没有就  默认为  false
        boolean remember_password = sharedPreferences.getBoolean("remember_password", false);

        if (remember_password) {
            String userName = null;
            String passWord = null;
            try {
                userName = EncrypAESUtil.decrypt(EncrypAESUtil.key, sharedPreferences.getString("userName", ""));
                passWord = EncrypAESUtil.decrypt(EncrypAESUtil.key, sharedPreferences.getString("passWord", ""));


                System.out.println("解密后的数据：" + "\n" + userName + "\n" + passWord);
            } catch (Exception e) {
                e.printStackTrace();
            }

//            EncrypAESUtil.encrypt2Java(EncrypAESUtil.key,sharedPreferences.getString("userName",""));
//            EncrypAESUtil.encrypt2Java(EncrypAESUtil.key,sharedPreferences.getString("passWord",""));


            etName.setText(userName);
            etPassword.setText(passWord);
        } else {
            etName.setText(sharedPreferences.getString("userName", ""));
        }

    }


    @OnClick(R.id.btnLogin)
    public void onViewClicked() {


        System.out.println("已点击按钮");

        final String userName = etName.getText().toString();
        final String pwd = etPassword.getText().toString();


        if (userName != null && !userName.equals("")) {
            if (pwd != null && !pwd.equals("")) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://172.31.236.63:8080/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                MyService service = retrofit.create(MyService.class);
                Call<List<User>> repos = service.Login(userName, pwd, "1", "a0:32:99:57:2c:68");


                repos.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        Log.d("response body", String.valueOf(response.body()));


                        System.out.println("==================================");
                        MainActivity.actionStart(LoginActivity.this, etName.getText().toString());
//                NewViewActivity.actionStart(LoginActivity.this, etName.getText().toString());

//                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                        //记住密码框选中，写入SharedPreferences
                        if (checkBox.isChecked()) {

                            String encode_userName = null;//加密后的
                            String encode_passWord = null;//加密后的
                            try {
                                encode_userName = EncrypAESUtil.encrypt2Java(EncrypAESUtil.key, userName);
                                encode_passWord = EncrypAESUtil.encrypt2Java(EncrypAESUtil.key, pwd);//加密后的


                                System.out.println("加密后的数据：" + "\n" + encode_userName + "\n" + encode_passWord);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            editor.putString("userName", encode_userName);
                            editor.putString("passWord", encode_passWord);
                            editor.putBoolean("remember_password", true);

                        } else {
                            editor.clear();
                            editor.putString("userName", userName);

                        }
                        editor.apply();
                        LoginActivity.this.finish();
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.i("response Throwable", t.getMessage().toString());
                    }
                });
            }
        } else {
            Toast.makeText(this, "请输入账户及密码！！！", Toast.LENGTH_SHORT).show();
        }


    }

}
