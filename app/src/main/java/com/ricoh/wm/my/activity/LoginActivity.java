package com.ricoh.wm.my.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.ricoh.wm.my.R;
import com.ricoh.wm.my.interfaces.MyService;
import com.ricoh.wm.my.model.User;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);



    }



    @OnClick(R.id.btnLogin)
    public void onViewClicked() {
        System.out.println("已点击按钮");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.31.236.63:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyService service = retrofit.create(MyService.class);
        Call<List<User>> repos = service.Login(etName.getText().toString(), etPassword.getText().toString(), "1", "a0:32:99:57:2c:68");


        repos.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.d("response body", String.valueOf(response.body()));


                System.out.println("==================================");
                MainActivity.actionStart(LoginActivity.this, etName.getText().toString());
//                NewViewActivity.actionStart(LoginActivity.this, etName.getText().toString());

                LoginActivity.this.finish();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("response Throwable", t.getMessage().toString());
            }
        });
    }
}
