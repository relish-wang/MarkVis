package com.hustoj.network;

import android.support.annotation.Keep;

import com.hustoj.bean.LoginResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Relish Wang
 * @since 2018/01/04
 */
@Keep
public interface HttpService {

    @GET("/csrf.php")
    Call<String> csrf();

    @POST("/login.php")
    @FormUrlEncoded
    Call<LoginResult> login(
            @Field(value = "user_id") String userId,
            @Field(value = "password") String pwdMD5,
            @Field(value = "vcode") String vcode,
            @Field(value = "csrf") String csrf);

    @POST("/submitpage.php")
    Call<String> submit();


    @POST("/submit.php")
    @FormUrlEncoded
    Call<String> submit(
            @Field(value = "id") String id,
            @Field(value = "language") int language,
            @Field(value = "vcode") String vcode,
            @Field(value = "source") String source,
            @Field(value = "csrf") String csrf);
}
