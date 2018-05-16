package com.ricoh.wm.my.interfaces;

import com.ricoh.wm.my.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Retrofit2  ******************************************************
 * Created by 2017063001 on 2018/3/9.
 */
public interface MyService {
    @FormUrlEncoded
    @POST("MyLoginServlet/myLoginServlet")
    Call<List<User>> Login(@Field("UserName") String username, @Field("PassWord") String passWord, @Field("ID") String id, @Field("MAC") String mac);

}
