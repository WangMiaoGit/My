package com.ricoh.wm.my.mvp.model;

import android.content.Context;
import android.util.Log;

import com.ricoh.wm.my.activity.MainActivity;
import com.ricoh.wm.my.interfaces.MyService;
import com.ricoh.wm.my.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author wangmiao
 * Created by 2017063001 on 2018/3/10.
 * mvp 模式中的 model层的实现类
 */
public class LoginModelImpl implements LoginModel {
    @Override
    public void login(final String userName, final String passWord, final Context context, final OnLoginListener onLoginListener) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.31.236.63:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyService service = retrofit.create(MyService.class);
        Call<List<User>> repos = service.Login(userName, passWord, "1", "a0:32:99:57:2c:68");

        repos.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("response body", String.valueOf(response.body()));

                //LoginModel中的OnLoginListener接口
                onLoginListener.loginSuccess(new User(userName, passWord));


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                //LoginModel中的OnLoginListener接口
                onLoginListener.loginFailed( t.getMessage().toString());
                Log.i("response Throwable", t.getMessage().toString());
            }
        });
    }
}
