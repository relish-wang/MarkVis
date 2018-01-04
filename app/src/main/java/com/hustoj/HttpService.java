package com.hustoj;

import android.support.annotation.Keep;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Relish Wang
 * @since 2018/01/04
 */
@Keep
public interface HttpService {

    @GET("/csrf.php")
    Call<String> csrf();
}
